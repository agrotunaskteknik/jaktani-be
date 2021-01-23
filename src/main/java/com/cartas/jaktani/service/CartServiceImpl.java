package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.CartItem;
import com.cartas.jaktani.model.Product;
import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.model.VwProductDetails;
import com.cartas.jaktani.repository.CartRepository;
import com.cartas.jaktani.repository.ProductRepository;
import com.cartas.jaktani.repository.ShopRepository;
import com.cartas.jaktani.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {
    Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    final static String INVALID_PARAM = "Invalid request param";
    final static String FAILED_SAVE_CART_DB = "Failed save cart db failed";
    final static String INVALID_CART_ID_NOT_FOUND_DB = "Cart ID not found in DB";
    final static String INVALID_CART_PRODUCT_OR_SHOP_NOT_SAME_DB = "Product or Shop is not the same in DB";
    final static String FAILED_SAVE_CART = "Failed save cart, empty id";
    final static String FAILED_UPDATE_CART = "Failed update cart, empty id";
    final static String SUCCESS_SAVE_CART = "Sukses menambahkan barang ke keranjang";
    final static String SUCCESS_UPDATE_CART = "Sukses memperbaharui keranjang";
    final static String FAILED_REMOVE_CART = "Failed remove cart";
    final static String SUCCESS_REMOVE_CART = "Sukses menghapus keranjang";
    final static String FAILED_CART_LIST = "Failed Cart List";
    final static String SUCCESS_CART_LIST_EMPTY = "Empty Cart List";
    final static String SUCCESS_CART_LIST = "Berhasil mendapatkan data";
    final static String STATUS_NOT_OK = "NOT_OK";
    final static String STATUS_OK = "OK";
    final static Integer CART_STATUS_DELETED = 0;
    final static Integer CART_STATUS_CART_PAGE = 1;
    final static Integer CART_STATUS_CHECKOUT = 3;

    @Autowired
    CartRepository cartRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    VwProductDetailsService vwProductDetailsService;

    public Boolean validationAddToCart(AddToCartDtoRequest addToCartDtoRequest) {
        boolean isValid = true;
        if (addToCartDtoRequest.getProductID() == null || addToCartDtoRequest.getProductID() == 0L) {
            isValid = false;
        }
        if (addToCartDtoRequest.getUserID() == null || addToCartDtoRequest.getUserID() == 0L) {
            isValid = false;
        }
        if (addToCartDtoRequest.getShopID() == null || addToCartDtoRequest.getShopID() == 0L) {
            isValid = false;
        }
        if (addToCartDtoRequest.getQuantity() == null || addToCartDtoRequest.getQuantity() == 0L) {
            isValid = false;
        }
        return isValid;
    }

    public Boolean validationUpdateToCart(AddToCartDtoRequest addToCartDtoRequest) {
        Boolean isValid = validationAddToCart(addToCartDtoRequest);
        if (addToCartDtoRequest.getCartID() == null || addToCartDtoRequest.getCartID() == 0L) {
            isValid = false;
        }
        return isValid;
    }

    public Boolean validationRemoveCart(RemoveCartDto param) {
        boolean isValid = true;
        if (param.getUserID() == null || param.getUserID() == 0L) {
            isValid = false;
        }
        if (param.getCartID() == null || param.getCartID() == 0L) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public AddToCartDtoResponse addToCart(AddToCartDtoRequest param) {
        AddToCartDtoResponse response = new AddToCartDtoResponse();
        // check request param
        if (param == null) {
            logger.error("Null param from client ");
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        Boolean isValidParam = validationAddToCart(param);
        if (!isValidParam) {
            logger.error("Empty param from client : " + param.toString());
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }

        // check if product exist
        Optional<Product> product = productRepository.findByIdAndStatusIsNot(param.getProductID().intValue(), ProductServiceImpl.STATUS_DELETED);
        if (!product.isPresent()) {
            response.setMessage("Product Not Found");
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        // check if shop exist
        Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(param.getShopID().intValue(), ProductServiceImpl.STATUS_DELETED);
        if (!shop.isPresent()) {
            response.setMessage("Shop Not Found");
            response.setStatus(STATUS_NOT_OK);
            return response;
        }

        // insert or update logic by checking product id and cart status
        CartItem saveCartParam = new CartItem();
        saveCartParam.setUserID(param.getUserID());
        saveCartParam.setProductID(param.getProductID());
        saveCartParam.setShopID(param.getShopID());
        saveCartParam.setQuantity(param.getQuantity());
        saveCartParam.setPrice(1000L);
        saveCartParam.setNotes(param.getNotes());
        saveCartParam.setStatus(CART_STATUS_CART_PAGE);
        Optional<CartItem> cartDB = cartRepository.findByProductIDAndStatusAndUserID(param.getProductID(), CART_STATUS_CART_PAGE, param.getUserID());
        if (cartDB != null && cartDB.isPresent()) {
            saveCartParam.setId(cartDB.get().getId());
            saveCartParam.setCreatedTime(cartDB.get().getCreatedTime());
            saveCartParam.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            saveCartParam.setQuantity(param.getQuantity() + cartDB.get().getQuantity());
        } else {
            saveCartParam.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        }
        try {
            CartItem saveCartResponse = cartRepository.save(saveCartParam);
            if (saveCartResponse.getId() == null || saveCartResponse.getId() == 0L) {
                response.setMessage(FAILED_SAVE_CART);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
            List<CartItem> cartItemList = cartRepository.findByStatusAndUserID(CART_STATUS_CART_PAGE, param.getUserID());
            long cartCounter = 0L;
            if (cartItemList != null && cartItemList.size() > 0) {
                cartCounter = cartItemList.size();
            }
            response.setCartCounter(cartCounter);
            response.setMessage(SUCCESS_SAVE_CART);
            response.setStatus(STATUS_OK);
            return response;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            response.setMessage(FAILED_SAVE_CART_DB);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
    }

    @Override
    public AddToCartDtoResponse getCounter(Long userID) {
        AddToCartDtoResponse response = new AddToCartDtoResponse();
        List<CartItem> cartItemList = cartRepository.findByStatusAndUserID(CART_STATUS_CART_PAGE, userID);
        long cartCounter = 0L;
        if (cartItemList != null && cartItemList.size() > 0) {
            cartCounter = cartItemList.size();
        }
        response.setCartCounter(cartCounter);
        response.setMessage(SUCCESS_SAVE_CART);
        response.setStatus(STATUS_OK);
        return response;
    }

    @Override
    public CommonResponse removeCart(RemoveCartDto param) {
        CommonResponse response = new CommonResponse();

        // check request param
        if (param == null) {
            logger.error("Null param from client ");
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        Boolean isValidParam = validationRemoveCart(param);
        if (!isValidParam) {
            logger.error("Empty param from client : " + param.toString());
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }

        // check if exist
        Optional<CartItem> cartDB = cartRepository.findByIdAndStatusAndUserID(param.getCartID(), CART_STATUS_CART_PAGE, param.getUserID());
        if (!cartDB.isPresent()) {
            logger.error(INVALID_CART_ID_NOT_FOUND_DB);
            response.setMessage(INVALID_CART_ID_NOT_FOUND_DB);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }

        // then remove
        CartItem saveCartParam = new CartItem();
        saveCartParam.setUserID(param.getUserID());
        saveCartParam.setProductID(cartDB.get().getProductID());
        saveCartParam.setShopID(cartDB.get().getShopID());
        saveCartParam.setQuantity(cartDB.get().getQuantity());
        saveCartParam.setPrice(cartDB.get().getPrice());
        saveCartParam.setStatus(CART_STATUS_DELETED);
        saveCartParam.setNotes(cartDB.get().getNotes());
        saveCartParam.setId(cartDB.get().getId());
        saveCartParam.setCreatedTime(cartDB.get().getCreatedTime());
        saveCartParam.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        try {
            CartItem saveCartResponse = cartRepository.save(saveCartParam);
            if (saveCartResponse.getId() == null || saveCartResponse.getId() == 0L) {
                response.setMessage(FAILED_REMOVE_CART);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
            response.setMessage(SUCCESS_REMOVE_CART);
            response.setStatus(STATUS_OK);
            return response;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            response.setMessage(FAILED_REMOVE_CART);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
    }

    @Override
    public CommonResponse updateCart(AddToCartDtoRequest param) {
        CommonResponse response = new CommonResponse();

        // check request param
        if (param == null) {
            logger.error("Null param from client ");
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        Boolean isValidParam = validationUpdateToCart(param);
        if (!isValidParam) {
            logger.error("Empty param from client : " + param.toString());
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        // check if cart id exist and product and shop the same
        Optional<CartItem> cartDB = cartRepository.findByIdAndProductIDAndStatusAndUserID(param.getCartID(), param.getProductID(), CART_STATUS_CART_PAGE, param.getUserID());
        if (!cartDB.isPresent()) {
            logger.error(INVALID_CART_ID_NOT_FOUND_DB);
            response.setMessage(INVALID_CART_ID_NOT_FOUND_DB);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        // check if product exist
        Optional<Product> product = productRepository.findByIdAndStatusIsNot(param.getProductID().intValue(), ProductServiceImpl.STATUS_DELETED);
        if (!product.isPresent()) {
            response.setMessage("Product Not Found");
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        // check if shop exist
        Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(param.getShopID().intValue(), ProductServiceImpl.STATUS_DELETED);
        if (!shop.isPresent()) {
            response.setMessage("Shop Not Found");
            response.setStatus(STATUS_NOT_OK);
            return response;
        }

        // update logic by checking product id and cart status and cart id
        CartItem saveCartParam = new CartItem();
        saveCartParam.setUserID(param.getUserID());
        saveCartParam.setProductID(param.getProductID());
        saveCartParam.setShopID(param.getShopID());
        saveCartParam.setQuantity(param.getQuantity());
        saveCartParam.setPrice(1000L);
        saveCartParam.setStatus(CART_STATUS_CART_PAGE);
        saveCartParam.setId(cartDB.get().getId());
        saveCartParam.setNotes(param.getNotes());
        saveCartParam.setCreatedTime(cartDB.get().getCreatedTime());
        saveCartParam.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        try {
            CartItem saveCartResponse = cartRepository.save(saveCartParam);
            if (saveCartResponse.getId() == null || saveCartResponse.getId() == 0L) {
                response.setMessage(FAILED_UPDATE_CART);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
            response.setMessage(SUCCESS_UPDATE_CART);
            response.setStatus(STATUS_OK);
            return response;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            response.setMessage(FAILED_UPDATE_CART);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
    }

    @Override
    public CartListResponse cartList(CartListDtoRequest cartListDtoRequest) {
        CartListResponse cartListResponse = new CartListResponse();
        if (cartListDtoRequest == null || cartListDtoRequest.getUserID() == 0) {
            logger.debug("Empty Param");
            cartListResponse.setErrorMessage(FAILED_CART_LIST);
            cartListResponse.setStatus(STATUS_NOT_OK);
            return cartListResponse;
        }
        List<CartItem> cartItemList = cartRepository.findByStatusAndUserID(CART_STATUS_CART_PAGE, cartListDtoRequest.getUserID());
//        // mock data
//        for (CartItem data : cartItemList) {
//            VwProductDetails product = new VwProductDetails();
//            product.setProductId(data.getProductID().intValue());
//            productMap.put(data.getProductID(), product);
//
//            Shop shop = new Shop();
//            shop.setId(data.getShopID().intValue());
//            shop.setStatus(1);
//            shopMap.put(data.getShopID(), shop);
//        }
        if (cartItemList.size() == 0) {
            logger.debug("Cart List Empty, userID = " + cartListDtoRequest.getUserID());
            cartListResponse.setShopGroupUnavailable(new ArrayList<>());
            cartListResponse.setShopGroupAvailable(new ArrayList<>());
            cartListResponse.setErrorMessage(SUCCESS_CART_LIST_EMPTY);
            cartListResponse.setStatus(STATUS_OK);
            return cartListResponse;
        }

        // get product info
        HashMap<Long, VwProductDetails> productMap = new HashMap<>();

        // get shop info
        HashMap<Long, Shop> shopMap = new HashMap<>();
        for (CartItem cartItem : cartItemList) {
            VwProductDetails product = vwProductDetailsService.findByProductIdProductDetails(cartItem.getProductID().intValue());
            if (product != null) {
                productMap.put(product.getProductId().longValue(), product);
            }

            Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.STATUS_DELETED);
            shop.ifPresent(value -> shopMap.put(value.getId().longValue(), value));
        }


        // sort by shop loop cartItemList, then get shop and product and save to HM, and in HM check the status available to sort
        List<CartDetails> listCartDetails = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            // check if list product null or size == 0, if yes then initiate, else just add the product from map
            VwProductDetails product = new VwProductDetails();
            if (productMap.get(cartItem.getProductID()) != null) {
                product = productMap.get(cartItem.getProductID());
            } else {
                // product not found the continue
                logger.debug("Product not found for product_id : " + cartItem.getProductID());
                continue;
            }
            CartDetails cartDetail = new CartDetails();
            cartDetail.setId(cartItem.getId());
            cartDetail.setVWProductDto(product);
            cartDetail.setNotes(cartItem.getNotes());
            cartDetail.setQuantity(cartItem.getQuantity());
            cartDetail.setPrice(cartItem.getPrice());
            listCartDetails.add(cartDetail);
        }


        HashMap<Long, ShopGroupData> shopGroupAvailableMap = new HashMap<>();
        HashMap<Long, ShopGroupData> shopGroupUnavailableMap = new HashMap<>();
        // check if it available or unavailable (validation product)
        for (CartDetails cartDetail : listCartDetails) {
            ShopGroupData shopGroupData = new ShopGroupData();
            if (shopMap.get(cartDetail.getVWProductDto().getShopId().longValue()) != null) {
                shopGroupData.setShop(shopMap.get(cartDetail.getVWProductDto().getShopId().longValue()));
                if (shopGroupData.getShop().getStatus() == 2) {
                    shopGroupData.setTickerMessage("Toko tutup");
                    // unavailable, get from map, and put it
                    ShopGroupData shopGroupData1 = shopGroupUnavailableMap.get(cartDetail.getVWProductDto().getShopId().longValue());
                    if (shopGroupData1 != null) {
                        shopGroupData1.getCartDetails().add(cartDetail);
                        shopGroupUnavailableMap.put(cartDetail.getVWProductDto().getShopId().longValue(), shopGroupData1);
                    } else {
                        List<CartDetails> cartDetails = new ArrayList<>();
                        cartDetails.add(cartDetail);
                        shopGroupData.setCartDetails(cartDetails);
                        shopGroupUnavailableMap.put(cartDetail.getVWProductDto().getShopId().longValue(), shopGroupData);
                    }
                    continue;
                }
            } else {
                // shop not found the continue
                logger.debug("Product not found for shop_id : " + cartDetail.getVWProductDto().getShopId());
                continue;
            }


            if (cartDetail.getVWProductDto().getStock() < cartDetail.getQuantity()) {
                cartDetail.getVWProductDto().setTickerMessage("Quantity is more than stock");
                // unavailable, get from map, and put it
                ShopGroupData shopGroupData1 = shopGroupUnavailableMap.get(cartDetail.getVWProductDto().getShopId().longValue());
                if (shopGroupData1 != null) {
                    shopGroupData1.getCartDetails().add(cartDetail);
                    shopGroupUnavailableMap.put(cartDetail.getVWProductDto().getShopId().longValue(), shopGroupData1);
                } else {
                    List<CartDetails> cartDetails = new ArrayList<>();
                    cartDetails.add(cartDetail);
                    shopGroupData.setCartDetails(cartDetails);
                    shopGroupUnavailableMap.put(cartDetail.getVWProductDto().getShopId().longValue(), shopGroupData);
                }
                continue;
            }
            ShopGroupData shopGroupDataA = shopGroupAvailableMap.get(cartDetail.getVWProductDto().getShopId().longValue());
            if (shopGroupDataA != null) {
                shopGroupDataA.getCartDetails().add(cartDetail);
                shopGroupAvailableMap.put(cartDetail.getVWProductDto().getShopId().longValue(), shopGroupDataA);
            } else {
                List<CartDetails> cartDetails = new ArrayList<>();
                cartDetails.add(cartDetail);
                shopGroupData.setCartDetails(cartDetails);
                shopGroupAvailableMap.put(cartDetail.getVWProductDto().getShopId().longValue(), shopGroupData);
            }
        }


        List<ShopGroupData> shopGroupAvailable = new ArrayList<>();
        List<ShopGroupData> shopGroupUnavailable = new ArrayList<>();
        for (Long key : shopGroupAvailableMap.keySet()) {
            shopGroupAvailable.add(shopGroupAvailableMap.get(key));
        }
        for (Long key : shopGroupUnavailableMap.keySet()) {
            shopGroupUnavailable.add(shopGroupUnavailableMap.get(key));
        }
        cartListResponse.setShopGroupAvailable(shopGroupAvailable);
        cartListResponse.setShopGroupUnavailable(shopGroupUnavailable);
        cartListResponse.setErrorMessage(SUCCESS_CART_LIST);
        cartListResponse.setStatus(STATUS_OK);
        return cartListResponse;
    }
}
