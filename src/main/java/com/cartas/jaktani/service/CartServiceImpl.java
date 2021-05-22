package com.cartas.jaktani.service;

import com.cartas.jaktani.controller.SendMail;
import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.model.*;
import com.cartas.jaktani.repository.*;
import com.cartas.jaktani.util.Utils;
import com.google.gson.Gson;
import okhttp3.*;
import org.apache.catalina.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
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
    final static Integer CART_STATUS_CHECKOUT = 2;
    final static String MIDTRANS_STATUS_SETTLEMENT = "settlement";
    final static Integer ORDER_STATUS_WAITING_PAYMENT_METHOD = 88;
    final static Integer ORDER_STATUS_WAITING_PAYMENT = 1;
    final static Integer ORDER_STATUS_PAYMENT_SETTLED = 2;
    final static Integer ORDER_STATUS_WAITING_SELLER_CONFIRMATION = 3;
    final static Integer ORDER_STATUS_SELLER_PROCESSING = 4;
    final static Integer ORDER_STATUS_SELLER_REJECTED = 8;
    final static Integer ORDER_STATUS_SHIPPING = 5;
    final static Integer ORDER_STATUS_WAITING_FOR_REVIEW = 6;
    final static Integer ORDER_STATUS_DONE = 7;
    final static String ORDER_STATUS_WAITING_PAYMENT_METHOD_TITLE = "Menunggu metode pembayaran";
    final static String ORDER_STATUS_WAITING_PAYMENT_TITLE = "Menunggu pembayaran";
    final static String ORDER_STATUS_PAYMENT_SETTLED_TITLE = "Pembayaran Terverifikasi";
    final static String ORDER_STATUS_WAITING_SELLER_CONFIRMATION_TITLE = "Menunggu konfirmasi Seller";
    final static String ORDER_STATUS_SELLER_PROCESSING_TITLE = "Diproses";
    final static String ORDER_STATUS_SELLER_REJECTED_TITLE = "Tidak Diproses";
    final static String ORDER_STATUS_SHIPPING_TITLE = "Dikirim";
    final static String ORDER_STATUS_WAITING_FOR_REVIEW_TITLE = "Tulis Review";
    final static String ORDER_STATUS_DONE_TITLE = "Selesai";
    public static String staticKey = "cart_";
    public static Integer ORDER_VERIFY_STATUS_CONFIRM = 1;
    public static Integer ORDER_VERIFY_STATUS_REJECT = 2;
    final static String XENDIT_URL = "https://api.xendit.co";
    final static String XENDIT_CALLBACK_VA = "/callback_virtual_accounts";
    final static String XENDIT_BASIC_TEST = "Basic eG5kX2RldmVsb3BtZW50X1A0cURmT3NzME9DcGw4UnRLclJPSGphUVlOQ2s5ZE41bFNmaytSMWw5V2JlK3JTaUN3WjNqdz09Og==";

    // one instance, reuse
    private final OkHttpClient httpClient = new OkHttpClient();
    Gson gson = new Gson();


    @Autowired
    CartRepository cartRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    ShopRepository shopRepository;
    @Autowired
    VwProductDetailsService vwProductDetailsService;
    @Autowired
    AddressService addressService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;

    @Autowired
    CartCacheRepository cacheRepository;

    @Autowired
    MultiCartCacheRepository multiCacheRepository;

    public String insertData(CartCache cache) {
        cache.setId(staticKey + cache.getUserID());
        System.out.println(cache);
        cache = cacheRepository.save(cache);
        return cache.toString();
    }

    public String insertMultiData(MultiCartCache cache) {
        cache.setId(staticKey + cache.getUserID());
        System.out.println(cache);
        cache = multiCacheRepository.save(cache);
        return cache.toString();
    }

    public List<CartCache> getAllDatas() {
        List<CartCache> students = new ArrayList<>();
        cacheRepository.findAll().forEach(students::add);
        return students;
    }

    public MultiCartCache getByCacheId(String userID) {
        MultiCartCache multiCartCache = new MultiCartCache();
        String cacheKey = staticKey + userID;
        System.out.println(cacheKey);
//        getAllDatas();
        Optional<MultiCartCache> multiCartCacheOptional = multiCacheRepository.findById(cacheKey);
        if (multiCartCacheOptional.isPresent()) {
            System.out.println(multiCartCache);
            return multiCartCacheOptional.get();
        }
        System.out.println("null for multi cache = " + cacheKey);
        Optional<CartCache> retrievedStudent = cacheRepository.findById(cacheKey);
        if (!retrievedStudent.isPresent()) {
            System.out.println("null for cache = " + cacheKey);
            return null;
        }
        multiCartCache.setUserID(Long.parseLong(userID));
        multiCartCache.setId(retrievedStudent.get().getId());
        List<CartCache> cartCacheList = new ArrayList<>();
        cartCacheList.add(retrievedStudent.get());
        multiCartCache.setCartCacheList(cartCacheList);
        return multiCartCache;
    }

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
            CartCache cartCache = new CartCache("", saveCartResponse.getId(), saveCartResponse.getUserID(), saveCartResponse.getShopID(),
                    saveCartResponse.getProductID(), saveCartResponse.getPrice(), saveCartResponse.getStatus(), saveCartResponse.getQuantity(),
                    saveCartResponse.getTransactionID(), saveCartResponse.getNotes());
            String insertCache = insertData(cartCache);
            System.out.println("insertCache = " + insertCache);
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

    @Override
    public SAFDtoResponse shipmentAddressForm(CartListDtoRequest cartListDtoRequest) {
//        CartListResponse cartListResponse = new CartListResponse();
        SAFDtoResponse safDtoResponse = new SAFDtoResponse();
        List<String> tickers = new ArrayList<>();
        List<GroupAddress> groupAddresses = new ArrayList<>();
        safDtoResponse.setTickers(tickers);
        safDtoResponse.setGroupAddress(groupAddresses);

        if (cartListDtoRequest == null || cartListDtoRequest.getUserID() == 0) {
            logger.debug("Empty Param");
            safDtoResponse.setErrorMessage(FAILED_CART_LIST);
            safDtoResponse.setStatus(STATUS_NOT_OK);
            return safDtoResponse;
        }
//        List<CartItem> cartItemList = cartRepository.findByStatusAndUserID(CART_STATUS_CART_PAGE, cartListDtoRequest.getUserID());
        MultiCartCache multiCartCache = getByCacheId(cartListDtoRequest.getUserID().toString());
        List<CartItem> cartItemList = new ArrayList<>();
        if (null != multiCartCache.getCartCacheList() && multiCartCache.getCartCacheList().size() > 0) {
            for (CartCache cache : multiCartCache.getCartCacheList()) {
                CartItem cartItemCache = new CartItem();
                cartItemCache.setNotes(cache.getNotes());
                cartItemCache.setQuantity(cache.getQuantity());
                cartItemCache.setStatus(cache.getStatus());
                cartItemCache.setPrice(cache.getPrice());
                cartItemCache.setShopID(cache.getShopID());
                cartItemCache.setProductID(cache.getProductID());
                cartItemCache.setUserID(cache.getUserID());
                cartItemCache.setId(cache.getCartId());
                cartItemCache.setTransactionID(cache.getTransactionID());
                cartItemList.add(cartItemCache);
            }
        }

        if (cartItemList.size() == 0) {
            logger.debug("Cart List Empty, userID = " + cartListDtoRequest.getUserID());
            safDtoResponse.setErrorMessage(SUCCESS_CART_LIST_EMPTY);
            safDtoResponse.setStatus(STATUS_OK);
            return safDtoResponse;
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

        AddressDetailDto defaultDto = addressService.getUserDefaultAddress(cartListDtoRequest.getUserID().intValue());
        for (Long key : shopGroupAvailableMap.keySet()) {
            GroupAddress groupAddress = new GroupAddress();
            ShopGroupData shopGroupData = shopGroupAvailableMap.get(key);
            groupAddress.setUserAddress(defaultDto);
            List<GroupShopDto> groupShops = new ArrayList<>();
            for (CartDetails cd : shopGroupData.getCartDetails()) {
                GroupShopDto groupShopDto = new GroupShopDto();
                groupShopDto.setCartString(cd.getId().toString());
                groupShopDto.setErrors(new ArrayList<>());
                groupShopDto.setShippingId(0L);
                Shop shopData = shopGroupData.getShop();
                AddressDetailDto defaultShopDto = addressService.getShopDefaultAddress(shopData.getId());
                shopData.setAddressDetailDto(defaultShopDto);
                groupShopDto.setShop(shopData);
                groupShopDto.setSpId(0L);
                groupShopDto.setQty(cd.getQuantity());
                groupShopDto.setVwProductDto(cd.getVWProductDto());
                groupShops.add(groupShopDto);
            }
            groupAddress.setGroupShop(groupShops);
            groupAddress.setErrors(new ArrayList<>());
            groupAddresses.add(groupAddress);
        }
        safDtoResponse.setTickers(tickers);
        safDtoResponse.setGroupAddress(groupAddresses);
        safDtoResponse.setErrorMessage(SUCCESS_CART_LIST);
        safDtoResponse.setStatus(STATUS_OK);
        return safDtoResponse;
    }

    @Override
    public CheckoutDtoResponse checkout(CheckoutDtoRequest cartListDtoRequest) throws IOException {
        CheckoutDtoResponse response = new CheckoutDtoResponse();
        CheckoutDtoData data = new CheckoutDtoData();
        List<CheckoutProductData> productList = new ArrayList<>();


        List<String> tickers = new ArrayList<>();
        List<GroupAddress> groupAddresses = new ArrayList<>();

        if (cartListDtoRequest == null || cartListDtoRequest.getUserId() == 0) {
            logger.debug("Empty Param");
            response.setErrorMessage(FAILED_CART_LIST);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        HashMap<Long, CheckoutShopProduct> checkoutShopProductByCartId = new HashMap<>();
        for (CheckoutShopProduct checkoutShopProduct : cartListDtoRequest.getShopProducts()) {
            checkoutShopProductByCartId.put(checkoutShopProduct.getCartId(), checkoutShopProduct);
        }

        // get cache for comparison
        MultiCartCache multiCartCache = getByCacheId(cartListDtoRequest.getUserId().toString());
        List<CartItem> cartItemList = new ArrayList<>();
        if (null != multiCartCache.getCartCacheList() && multiCartCache.getCartCacheList().size() > 0) {
            for (CartCache cache : multiCartCache.getCartCacheList()) {
                CartItem cartItemCache = new CartItem();
                cartItemCache.setNotes(cache.getNotes());
                cartItemCache.setQuantity(cache.getQuantity());
                cartItemCache.setStatus(cache.getStatus());
                cartItemCache.setPrice(cache.getPrice());
                cartItemCache.setShopID(cache.getShopID());
                cartItemCache.setProductID(cache.getProductID());
                cartItemCache.setUserID(cache.getUserID());
                cartItemCache.setId(cache.getCartId());
                cartItemCache.setTransactionID(cache.getTransactionID());
                cartItemList.add(cartItemCache);
            }
        }
        if (cartItemList.size() == 0) {
            logger.debug("Cart List Empty, userID = " + cartListDtoRequest.getUserId());
            response.setErrorMessage(SUCCESS_CART_LIST_EMPTY);
            response.setStatus(STATUS_OK);
            return response;
        }
        List<CartItem> cartItemFromDB = new ArrayList<>();
        // get product info
        HashMap<Long, VwProductDetails> productMap = new HashMap<>();
        // get shop info
        HashMap<Long, Shop> shopMap = new HashMap<>();
        for (CartItem cartItem : cartItemList) {
            // get db data
            Optional<CartItem> cartItemOptional = cartRepository.findByIdAndStatusAndUserID(cartItem.getId(),
                    CART_STATUS_CART_PAGE, cartItem.getUserID());
            if (!cartItemOptional.isPresent()) {
                logger.debug("Cart id not found in db");
                response.setErrorMessage(FAILED_CART_LIST);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
            cartItemFromDB.add(cartItemOptional.get());

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
        CheckoutParameterResponse checkoutParameterResponse = new CheckoutParameterResponse();
        for (Long key : shopGroupAvailableMap.keySet()) {
            ShopGroupData shopGroupData = shopGroupAvailableMap.get(key);
            Long grossAmount = 0L;
            for (CartDetails cartDetails : shopGroupData.getCartDetails()) {
                CheckoutProductData productData = new CheckoutProductData();
                grossAmount += cartDetails.getPrice();
                CheckoutShopProduct checkoutShopProduct = checkoutShopProductByCartId.get(cartDetails.getId());
                CostParent costParent = addressService.getCostByCityId(checkoutShopProduct.getOriginCityId().toString(), checkoutShopProduct.getDestincationCityId().toString(),
                        cartDetails.getVWProductDto().getSize().longValue(), checkoutShopProduct.getCourier());
                for (CostResult costResult : costParent.getRajaongkir().getResults()) {
                    if (costResult.getCode().trim().equalsIgnoreCase(checkoutShopProduct.getCourier())) {
                        for (CostResultDetail costResultDetail : costResult.getCosts()) {
                            if (costResultDetail.getService().trim().equalsIgnoreCase(checkoutShopProduct.getService())) {
                                for (CostResultDetailData costResultDetailData : costResultDetail.getCost()) {
                                    grossAmount += costResultDetailData.getValue();
                                    productData.setId(cartDetails.getId().toString());
                                    productData.setName(cartDetails.getVWProductDto().getProductName());
                                    productData.setPrice(cartDetails.getVWProductDto().getPrice().longValue());
                                    productData.setQuantity(cartDetails.getQuantity());
                                    productList.add(productData);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            checkoutParameterResponse.setGrossAmount(grossAmount);
            checkoutParameterResponse.setCustomerId(cartListDtoRequest.getUserId());
        }
        // save gross amount and get order id
        Order order = new Order();
        order.setCustomerId(cartListDtoRequest.getUserId());
        order.setGrossAmount(checkoutParameterResponse.getGrossAmount());
        if (cartListDtoRequest.getShopProducts().size() != 0) {
            order.setShopId(cartListDtoRequest.getShopProducts().get(0).getShopId());
        }
        order.setStatus(ORDER_STATUS_WAITING_PAYMENT_METHOD);
        order.setQuantity(cartItemList.get(0).getQuantity().intValue());
        order.setCustAddress(Integer.valueOf(cartListDtoRequest.getAddressId()));
        order.setShopId(cartItemList.get(0).getShopID());
        order.setCourier(cartListDtoRequest.getShopProducts().get(0).getCourier());
        order = orderRepository.save(order);

        System.out.println("order = " + order);

        for (CartItem cartItem : cartItemFromDB) {
            // update cart db
            CartItem cartItemUpdate = cartItem;
            cartItemUpdate.setStatus(CART_STATUS_CHECKOUT);
            cartItemUpdate.setTransactionID(order.getId());
            cartItemUpdate.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            cartItemUpdate = cartRepository.save(cartItemUpdate);
            System.out.println("cartItemUpdate = " + cartItemUpdate);

            // update decrease product stock
            Optional<Product> optionalProduct = productRepository.findById(cartItem.getProductID().intValue());
            if (optionalProduct.isPresent()) {
                Product product = optionalProduct.get();
                product.setStock(optionalProduct.get().getStock() - cartItem.getQuantity().intValue());
                productRepository.save(product);
            }
        }

        // delete cache
        multiCacheRepository.delete(multiCartCache);

        checkoutParameterResponse.setOrderId(order.getId());
        data.setProductList(productList);
        data.setParameter(checkoutParameterResponse);
        response.setData(data);
        response.setErrorMessage(SUCCESS_CART_LIST);
        response.setStatus(STATUS_OK);
        return response;
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    public PaymentChargeDtoResponse paymentChargeMidtrans(PaymentChargeRequest paymentChargeRequest) throws IOException {
        PaymentChargeDtoResponse responseDto;
        String json = "{\n" +
                "    \"payment_type\": \"{{paymentType}}\",\n" +
                "    \"transaction_details\": {\n" +
                "        \"order_id\": \"{{orderId}}\",\n" +
                "        \"gross_amount\": {{grossAmount}}\n" +
                "    },\n" +
                "    \"bank_transfer\": {\n" +
                "        \"bank\": \"{{bank}}\"\n" +
                "    }\n" +
                "}";
        json = json.replace("{{paymentType}}", paymentChargeRequest.getPaymentType());
        json = json.replace("{{orderId}}", paymentChargeRequest.getOrderId());
        json = json.replace("{{grossAmount}}", paymentChargeRequest.getGrossAmount());
        json = json.replace("{{bank}}", paymentChargeRequest.getBank());
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url("https://api.sandbox.midtrans.com/v2/charge")
                .post(body)
                .addHeader("Authorization", "Basic U0ItTWlkLXNlcnZlci1mY0V3R2kyb2xseldrU0xMVGtoSUpqYnc6")  // add request headers
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            PaymentChargeMidResponse entity = gson.fromJson(jsonString, PaymentChargeMidResponse.class);
            List<VaNumberDto> vaNumberDtos = new ArrayList<>();
            for (VaNumber vaNumber : entity.getVa_numbers()) {
                VaNumberDto vaNumberDto = new VaNumberDto();
                vaNumberDto.setBank(vaNumber.getBank());
                vaNumberDto.setVaNumber(vaNumber.getVa_number());
                vaNumberDtos.add(vaNumberDto);
            }
            String transactionTimeInMilis = entity.getTransaction_time();
            Double grossAmountDouble = Double.parseDouble(entity.getGross_amount());
            Long grossAmount = grossAmountDouble.longValue();

            // update order
            try {
                Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(entity.getOrder_id()));
                if (orderOptional.isPresent()) {
                    Order order = orderOptional.get();
                    order.setGrossAmount(grossAmount);
                    order.setCreatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                    order.setPaymentType(entity.getPayment_type());
                    order.setMetadata(jsonString);
                    order.setTransactionID(entity.getTransaction_id());
                    order.setTransactionStatus(entity.getTransaction_status());
                    order.setVaNumber(entity.getVa_numbers().get(0).getVa_number());
                    order.setStatus(ORDER_STATUS_WAITING_PAYMENT);
                    order = orderRepository.save(order);
                    paymentChargeRequest.setUserID(order.getCustomerId().intValue());
                    System.out.println("order = " + order);
                }
            } catch (Exception ex) {
                System.out.println("error : " + ex);
            }


            responseDto = new PaymentChargeDtoResponse(entity.getStatus_message(), entity.getTransaction_id(), entity.getOrder_id(), entity.getMerchant_id(),
                    grossAmount, entity.getGross_amount() + entity.getCurrency(), entity.getCurrency(), entity.getPayment_type(),
                    Utils.getCalendar().getTimeInMillis(), entity.getTransaction_status(), vaNumberDtos, entity.getFraud_status());
            emailForPaymentRequest(paymentChargeRequest, responseDto);
            return responseDto;
        }
    }

    @Override
    public PaymentChargeDtoResponse paymentCharge(PaymentChargeRequest paymentChargeRequest) throws IOException {
        // update order
        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(paymentChargeRequest.getOrderId()));
        if (!orderOptional.isPresent()){
            logger.debug("order id : "+paymentChargeRequest.getOrderId()+" is not found!");
            return new PaymentChargeDtoResponse();
        }

        String userFullName = "blank name";
        Optional<Users> userOptional = userRepository.findById(orderOptional.get().getCustomerId().intValue());
        if(userOptional.isPresent()){
            userFullName = userOptional.get().fullName;
        }

        PaymentChargeDtoResponse responseDto;
        String json = "{\n" +
                "    \"external_id\": \"{{orderID}}\",\n" +
                "    \"bank_code\": \"{{bank}}\",\n" +
                "    \"name\": \"{{fullName}}\",\n" +
                "    \"expected_amount\": {{grossAmount}}\n" +
                "}";
        json = json.replace("{{fullName}}", userFullName);
        json = json.replace("{{orderID}}", paymentChargeRequest.getOrderId());
        json = json.replace("{{grossAmount}}", paymentChargeRequest.getGrossAmount());
        json = json.replace("{{bank}}", paymentChargeRequest.getBank().toUpperCase());
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(XENDIT_URL+XENDIT_CALLBACK_VA)
                .post(body)
                .addHeader("Authorization", XENDIT_BASIC_TEST)  // add request headers
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            CallbackVAXendit entity = gson.fromJson(jsonString, CallbackVAXendit.class);
            String transactionTimeInMilis = Utils.getCalendar().getTimeInMillis()+"";
            Double grossAmountDouble = Double.parseDouble(entity.getExpected_amount().toString());
            Long grossAmount = grossAmountDouble.longValue();

            try {
                Order order = orderOptional.get();
                order.setGrossAmount(grossAmount);
                order.setCreatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                order.setPaymentType("xendit");
                order.setMetadata(jsonString);
                order.setTransactionID(entity.getExternal_id());
//                order.setTransactionStatus(entity.getTransaction_status());
//                order.setVaNumber(entity.getVa_numbers().get(0).getVa_number());
                order.setStatus(ORDER_STATUS_WAITING_PAYMENT);
                order = orderRepository.save(order);
                paymentChargeRequest.setUserID(order.getCustomerId().intValue());
                System.out.println("order = " + order);
            } catch (Exception ex) {
                logger.debug("error pas set order : " + ex.getMessage());
            }

            responseDto = new PaymentChargeDtoResponse(entity.getStatus_message(), entity.getTransaction_id(), entity.getOrder_id(), entity.getMerchant_id(),
                    grossAmount, entity.getGross_amount() + entity.getCurrency(), entity.getCurrency(), entity.getPayment_type(),
                    Utils.getCalendar().getTimeInMillis(), entity.getTransaction_status(), vaNumberDtos, entity.getFraud_status());
            emailForPaymentRequest(paymentChargeRequest, responseDto);
            return responseDto;
        }
    }

    @Override
    public PaymentChargeDtoResponse paymentCheckStatus(String orderId) throws IOException {
        PaymentChargeDtoResponse responseDto;
        String url = "https://api.sandbox.midtrans.com/v2/{{orderId}}/status";
        url = url.replace("{{orderId}}", orderId);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Basic U0ItTWlkLXNlcnZlci1mY0V3R2kyb2xseldrU0xMVGtoSUpqYnc6")  // add request headers
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            PaymentChargeMidResponse entity = gson.fromJson(jsonString, PaymentChargeMidResponse.class);
            List<VaNumberDto> vaNumberDtos = new ArrayList<>();
            for (VaNumber vaNumber : entity.getVa_numbers()) {
                VaNumberDto vaNumberDto = new VaNumberDto();
                vaNumberDto.setBank(vaNumber.getBank());
                vaNumberDto.setVaNumber(vaNumber.getVa_number());
                vaNumberDtos.add(vaNumberDto);
            }
            String transactionTimeInMilis = entity.getTransaction_time();
            Double grossAmountDouble = Double.parseDouble(entity.getGross_amount());
            Long grossAmount = grossAmountDouble.longValue();

            responseDto = new PaymentChargeDtoResponse(entity.getStatus_message(), entity.getTransaction_id(), entity.getOrder_id(), entity.getMerchant_id(),
                    grossAmount, entity.getGross_amount() + entity.getCurrency(), entity.getCurrency(), entity.getPayment_type(),
                    Utils.getCalendar().getTimeInMillis(), entity.getTransaction_status(), vaNumberDtos, entity.getFraud_status());
            if (responseDto.getTransactionStatus().equalsIgnoreCase(MIDTRANS_STATUS_SETTLEMENT)) {
                updateOrderToSettlement(orderId);
            }
            return responseDto;
        }
    }

    public void updateOrderToSettlement(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(orderId));
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Timestamp transactiontime = Utils.getTimeStamp(1L);
            if (order.getTransactionTime() != null) {
                transactiontime = order.getTransactionTime();
            }
            emailForPaymentAccepted("mockBCA", transactiontime.toString(), order.getCustomerId().intValue(), order.getGrossAmount());
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                order.setStatus(ORDER_STATUS_PAYMENT_SETTLED);
                order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                orderRepository.save(order);
                logger.debug("Success Update to Settlement: order id : " + orderId);
            }
        }
    }

    @Override
    public List<OrderDetailDto> orderStatusByUserID(Long userID) {
        List<OrderDetailDto> orderDetailDto = new ArrayList<>();
        List<Order> orderList = orderRepository.findByStatusIsNotAndCustomerId(0, userID);
        for (Order order : orderList) {
            // if waiting for payment then check if already updated (this function can be replaced if there are cron or listener to change status from midtrans)
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                try {
                    paymentCheckStatus(order.getId().toString());
                } catch (Exception ex) {
                    System.out.println("Exception check status order : " + ex.getMessage());
                }
                Optional<Order> orderChanged = orderRepository.findById(order.getId());
                if (orderChanged.isPresent()) {
                    order = orderChanged.get();
                }
            }
            // get product id by order id from cart
            List<CartItem> cartItemOptional = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, userID, order.getId());
            // get product detail by product id
            for (CartItem cartItem : cartItemOptional) {
                // get shop detail by product shop id
                VwProductDetails product = vwProductDetailsService.findByProductIdProductDetails(cartItem.getProductID().intValue());
                // get shipping detail by cart item detail
                Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.STATUS_DELETED);
                OrderDetailDto orderResp = new OrderDetailDto();
                orderResp.setIconImg("");
                String statusTitle = getStatusTitleByStatusId(order.getStatus());
                orderResp.setOrderStatusTitle(statusTitle);
                orderResp.setOrderStatus(order.getStatus());
                orderResp.setOrderTotal(order.getGrossAmount());
                orderResp.setOrderTotalAmount(order.getQuantity().longValue());
                orderResp.setOrderID(order.getId());
                // get detail waybill
                if (order.getStatus().equals(ORDER_STATUS_SHIPPING)) {
                    try {
                        orderResp.setDetailWaybill(addressService.getWaybillDetail(order.getResiCode(), order.getCourier()));
                    } catch (Exception ex) {
                        System.out.println("waybill error " + ex.getMessage());
                    }
                }
                Timestamp orderDate = Utils.getTimeStamp(1L);
                if (order.getCreatedDate() != null) {
                    orderDate = order.getCreatedDate();
                }
                orderResp.setOrderTransactionDateString(orderDate.toString());
                orderResp.setProduct(product);
                if (shop.isPresent()) {
                    Shop shopData = shop.get();
                    AddressDetailDto defaultShopDto = addressService.getShopDefaultAddress(shopData.getId());
                    shopData.setAddressDetailDto(defaultShopDto);
                    orderResp.setShop(shopData);
                }

                orderDetailDto.add(orderResp);
            }
        }
        return orderDetailDto;
    }

    @Override
    public List<OrderDetailDto> orderStatusByShopID(Long shopID) {
        List<OrderDetailDto> orderDetailDto = new ArrayList<>();
        List<CartItem> cartItemList = cartRepository.findByStatusIsNotAndShopID(CART_STATUS_DELETED, shopID);
        List<Long> orderIDs = new ArrayList<>();
        for (CartItem cartItem : cartItemList) {
            orderIDs.add(cartItem.getTransactionID());
        }
        List<Order> orderList = orderRepository.findByIdIn(orderIDs);
        HashMap<Long, UserDto> mapUserByID = new HashMap<>();
        for (Order order : orderList) {
            // if waiting for payment then check if already updated (this function can be replaced if there are cron or listener to change status from midtrans)
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                try {
                    paymentCheckStatus(order.getId().toString());
                } catch (Exception ex) {
                    System.out.println("Exception check status order : " + ex.getMessage());
                }
                Optional<Order> orderChanged = orderRepository.findById(order.getId());
                if (orderChanged.isPresent()) {
                    order = orderChanged.get();
                }
            }
            // get product id by order id from cart
            List<CartItem> cartItemOptional = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            // get product detail by product id
            for (CartItem cartItem : cartItemOptional) {
                // get shop detail by product shop id
                VwProductDetails product = vwProductDetailsService.findByProductIdProductDetails(cartItem.getProductID().intValue());
                // get shipping detail by cart item detail
                Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.STATUS_DELETED);
                OrderDetailDto orderResp = new OrderDetailDto();
                orderResp.setIconImg("");
                String statusTitle = getStatusTitleByStatusId(order.getStatus());
                orderResp.setOrderStatusTitle(statusTitle);
                orderResp.setOrderStatus(order.getStatus());
                orderResp.setOrderTotal(order.getGrossAmount());
                orderResp.setOrderTotalAmount(order.getQuantity().longValue());
                Timestamp orderDate = Utils.getTimeStamp(1L);
                if (order.getCreatedDate() != null) {
                    orderDate = order.getCreatedDate();
                }
                orderResp.setOrderTransactionDateString(orderDate.toString());
                orderResp.setOrderID(order.getId());
                orderResp.setProduct(product);
                if (shop.isPresent()) {
                    Shop shopData = shop.get();
                    AddressDetailDto defaultShopDto = addressService.getShopDefaultAddress(shopData.getId());
                    shopData.setAddressDetailDto(defaultShopDto);
                    orderResp.setShop(shopData);
                }

                // check map for user detail, if not exist then fetch it from db
                if (mapUserByID.isEmpty() || !mapUserByID.containsKey(cartItem.getUserID())) {
                    UserDto userDto = getUserDetail(cartItem.getUserID());
                    mapUserByID.put(cartItem.getUserID(), userDto);
                    logger.debug(userDto.toString());
                }

                orderResp.setUserDto(mapUserByID.get(cartItem.getUserID()));
                orderDetailDto.add(orderResp);
            }
        }
        return orderDetailDto;
    }

    public UserDto getUserDetail(Long userID) {
        //get user by username
        UserDto userDto = userService.getUserByID(userID);
        if (null == userDto) {
            return new UserDto();
        }

        // get user default address
        AddressDetailDto userAddress = addressService.getDefaultAddressByIdAndRelationType(userDto.getId(), AddressServiceImpl.TYPE_USER);
        userDto.setUserAddress(userAddress);

        // get shop by user id
        Shop shop = shopService.getShopObjectByUserID(userDto.getId());
        // get shop default address
        AddressDetailDto shopAddress = addressService.getDefaultAddressByIdAndRelationType(shop.getId(), AddressServiceImpl.TYPE_SHOP);
        userDto.setUserShopAddress(shopAddress);

        return userDto;
    }

    public String getStatusTitleByStatusId(Integer statusID) {
        String statusTitle = "";
        if (statusID.equals(ORDER_STATUS_WAITING_PAYMENT_METHOD)) {
            statusTitle = ORDER_STATUS_WAITING_PAYMENT_METHOD_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_WAITING_PAYMENT)) {
            statusTitle = ORDER_STATUS_WAITING_PAYMENT_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_PAYMENT_SETTLED)) {
            statusTitle = ORDER_STATUS_PAYMENT_SETTLED_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_WAITING_SELLER_CONFIRMATION)) {
            statusTitle = ORDER_STATUS_WAITING_SELLER_CONFIRMATION_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_SELLER_PROCESSING)) {
            statusTitle = ORDER_STATUS_SELLER_PROCESSING_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_SELLER_REJECTED)) {
            statusTitle = ORDER_STATUS_SELLER_REJECTED_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_SHIPPING)) {
            statusTitle = ORDER_STATUS_SHIPPING_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_WAITING_FOR_REVIEW)) {
            statusTitle = ORDER_STATUS_WAITING_FOR_REVIEW_TITLE;
        }
        if (statusID.equals(ORDER_STATUS_DONE)) {
            statusTitle = ORDER_STATUS_DONE_TITLE;
        }
        return statusTitle;
    }

    @Override
    public void sellerVerifyOrder(VerifyOrderShippingRequest request) {
        if (!request.getStatus().equals(ORDER_VERIFY_STATUS_CONFIRM)
                && !request.getStatus().equals(ORDER_VERIFY_STATUS_REJECT)) {
            logger.debug("status not found");
            return;
        }
        Optional<Order> orderOptional = orderRepository.findById(request.getOrderID());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (request.getStatus().equals(ORDER_VERIFY_STATUS_CONFIRM)) {
                order.setStatus(ORDER_STATUS_SELLER_PROCESSING);
                order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                orderRepository.save(order);
            } else if (request.getStatus().equals(ORDER_VERIFY_STATUS_REJECT)) {
                order.setStatus(ORDER_STATUS_SELLER_REJECTED);
                order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                orderRepository.save(order);
            }

        }
    }

    @Override
    public void sellerVerifyOrderShipping(VerifyOrderShippingRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(request.getOrderID());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(ORDER_STATUS_SHIPPING);
            order.setResiCode(request.getResiCode());
            order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            orderRepository.save(order);
        }
    }

    @Override
    public void sellerVerifyOrderSent(VerifyOrderShippingRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(request.getOrderID());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(ORDER_STATUS_WAITING_FOR_REVIEW);
            order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            orderRepository.save(order);
            List<CartItem> cartItems = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            for (CartItem cartItem : cartItems) {
                Optional<Product> product = productRepository.findById(cartItem.getProductID().intValue());
                product.ifPresent(value -> emailForOrderDone(order.getCustomerId().intValue(), value.getName()));
            }
        }
    }

    @Override
    public void sellerVerifyReview(VerifyOrderShippingRequest request) {
        Optional<Order> orderOptional = orderRepository.findById(request.getOrderID());
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(ORDER_STATUS_DONE);
            order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            orderRepository.save(order);
        }
    }

    @Override
    public CommonResponse updateCartV2(List<AddToCartDtoRequest> params) {
        CommonResponse response = new CommonResponse();

        // check request param
        if (params == null) {
            logger.error("Null param from client ");
            response.setMessage(INVALID_PARAM);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }

        for (AddToCartDtoRequest request : params) {
            Boolean isValidParam = validationUpdateToCart(request);
            if (!isValidParam) {
                logger.error("Empty param from client : " + params.toString());
                response.setMessage(INVALID_PARAM);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
        }

        List<CartCache> cartCacheList = new ArrayList<>();
        Long userID = 0L;
        for (AddToCartDtoRequest param : params) {
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
                CartCache cartCache = new CartCache("", saveCartResponse.getId(), saveCartResponse.getUserID(), saveCartResponse.getShopID(),
                        saveCartResponse.getProductID(), saveCartResponse.getPrice(), saveCartResponse.getStatus(), saveCartResponse.getQuantity(),
                        saveCartResponse.getTransactionID(), saveCartResponse.getNotes());
                cartCacheList.add(cartCache);
                userID = saveCartResponse.getUserID();
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                response.setMessage(FAILED_UPDATE_CART);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
        }
        MultiCartCache multiCartCache = new MultiCartCache();
        multiCartCache.setCartCacheList(cartCacheList);
        multiCartCache.setUserID(userID);
        String insertCache = insertMultiData(multiCartCache);
        System.out.println("insertMultiData = " + insertCache);
        response.setMessage(SUCCESS_UPDATE_CART);
        response.setStatus(STATUS_OK);
        return response;
    }

    public void emailForPaymentRequest(PaymentChargeRequest paymentChargeRequest, PaymentChargeDtoResponse paymentChargeDtoResponse) {
        Optional<Users> user = userRepository.findById(paymentChargeRequest.getUserID());
        if (user.isPresent()) {
            String messageBodyRegister = "Halo! Terimakasih telah melakukan transaksi checkout\n" +
                    "Silahkan lakukan pembayaran " + paymentChargeRequest.getBank().toUpperCase() + " dengan detail sebagai berikut :\n" +
                    "\n" +
                    "Total Bayar : " + paymentChargeDtoResponse.getGrossAmount() + "\n" +
                    "\n" +
                    "Metode Pembayaran : " + paymentChargeRequest.getBank().toUpperCase() + "\n" +
                    "\n" +
                    "Kode Virtual Account : " + paymentChargeDtoResponse.getVaNumberDto().get(0) + "\n" +
                    "\n" +
                    "Rincian Pesanan : \n" +
                    "\n" +
                    "\n" +
                    "Mohon melakukan pembayaran sebelum :" + paymentChargeDtoResponse.getTransactionTimeInMilis() + " \n" +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n";
            String messageSubjectRegister = "Menunggu Pembayaran " + paymentChargeRequest.getBank().toUpperCase() + " untuk pembayaran dengan order id " + paymentChargeRequest.getOrderId();
            sentEmail(user.get().getEmail(), messageBodyRegister, messageSubjectRegister);
        } else {
            logger.debug("Username/Email tidak ditemukan");
        }
    }

    public void emailForPaymentAccepted(String kodeBank, String tanggalBayar, Integer userID, Long totalBayar) {
        Optional<Users> user = userRepository.findById(userID);
        if (user.isPresent()) {
            String messageBodyRegister = "Halo! Pembayaran terverifikasi dan pesanan telah diteruskan ke penjual \n" +
                    "Terima kasih, ya! Berikut detail pembayaranmu :\n" +
                    "\n" +
                    "Total Bayar : " + totalBayar + "\n" +
                    "\n" +
                    "Metode Pembayaran : " + kodeBank.toUpperCase() + "\n" +
                    "\n" +
                    "Waktu Pembayaran : " + tanggalBayar + "\n" +
                    "\n" +
                    "Rincian Pesanan : \n" +
                    "\n" +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n";
            String messageSubjectRegister = "Checkout Pesanan dengan " + kodeBank.toUpperCase() + " Berhasil tanggal " + tanggalBayar;
            sentEmail(user.get().getEmail(), messageBodyRegister, messageSubjectRegister);
        } else {
            logger.debug("Username/Email tidak ditemukan");
        }
    }

    public void emailForOrderDone(Integer userID, String productName) {
        Optional<Users> user = userRepository.findById(userID);
        if (user.isPresent()) {
            String messageBodyRegister = "Halo! Yeay barangmu sudah sampai! \n" +
                    "Terimakasih sudah berbelanja dan mendukung para penjual di JakTani. \n" +
                    "\n" +
                    "Rincian Pesanan : \n" +
                    "\n" +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n";
            String messageSubjectRegister = "Pesanan Selesai: " + productName;
            sentEmail(user.get().getEmail(), messageBodyRegister, messageSubjectRegister);
        } else {
            logger.debug("Username/Email tidak ditemukan");
        }
    }

    public void sentEmail(String email, String messageBody, String messageSubject) {
        try {
            Thread newThread = new Thread(() -> {
                try {
                    SendMail.sentEmail(email, messageBody, messageSubject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            newThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            logger.debug("Error caught : " + e.getMessage());
        }

    }
}
