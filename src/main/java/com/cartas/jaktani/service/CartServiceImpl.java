package com.cartas.jaktani.service;

import com.cartas.jaktani.controller.SendMail;
import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.*;
import com.cartas.jaktani.repository.*;
import com.cartas.jaktani.util.Utils;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CartServiceImpl implements CartService {
    Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    final static Integer CART_SHOP_CLOSE = 10;
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
    final static String XENDIT_FVA_SIMULATE_PAYMENT = "callback_virtual_accounts/external_id={external_id}/simulate_payment";
    final static String XENDIT_VERIFY_CALLBACK_VA = "/callback_virtual_account_payments/payment_id={{payment_id}}";
    final static String XENDIT_BASIC_AUTH = "Basic eG5kX3Byb2R1Y3Rpb25fa080ZXRGaGt1R1Nzem94N1JvRENSSFE4WFhjSnhPOHVucHhjcmcxZXBOR2cwVlQ4MWdiYmhhRWxtdE51TTo=";

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
        logger.info(cache.toString());
        cache = cacheRepository.save(cache);
        return cache.toString();
    }

    public String insertMultiData(MultiCartCache cache) {
        cache.setId(staticKey + cache.getUserID());
        logger.info(cache.toString());
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
        logger.info(cacheKey);
//        getAllDatas();
        Optional<MultiCartCache> multiCartCacheOptional = multiCacheRepository.findById(cacheKey);
        if (multiCartCacheOptional.isPresent()) {
            logger.info(multiCartCache.toString());
            return multiCartCacheOptional.get();
        }
        logger.info("null for multi cache = " + cacheKey);
        Optional<CartCache> retrievedStudent = cacheRepository.findById(cacheKey);
        if (!retrievedStudent.isPresent()) {
            logger.info("null for cache = " + cacheKey);
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
        saveCartParam.setPrice(product.get().getPrice().longValue());
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
        saveCartParam.setPrice(product.get().getPrice().longValue());
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
            logger.info("insertCache = " + insertCache);
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
            logger.info("Empty Param");
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
            logger.info("Cart List Empty, userID = " + cartListDtoRequest.getUserID());
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

            Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.SHOP_STATUS_DELETED);
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
                logger.info("Product not found for product_id : " + cartItem.getProductID());
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
                if (shopGroupData.getShop().getStatus().equals(CART_SHOP_CLOSE)) {
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
                logger.info("Product not found for shop_id : " + cartDetail.getVWProductDto().getShopId());
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
            logger.info("Empty Param");
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
            logger.info("Cart List Empty, userID = " + cartListDtoRequest.getUserID());
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

            Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.SHOP_STATUS_DELETED);
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
                logger.info("Product not found for product_id : " + cartItem.getProductID());
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
                if (shopGroupData.getShop().getStatus().equals(CART_SHOP_CLOSE)) {
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
                logger.info("Product not found for shop_id : " + cartDetail.getVWProductDto().getShopId());
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
                groupShopDto.setNotes(cd.getNotes());
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
    public CheckoutDtoResponse checkout(CheckoutDtoRequest checkoutDtoRequest) throws IOException {
        CheckoutDtoResponse response = new CheckoutDtoResponse();
        CheckoutDtoData data = new CheckoutDtoData();
        List<CheckoutProductData> productList = new ArrayList<>();


        List<String> tickers = new ArrayList<>();
        List<GroupAddress> groupAddresses = new ArrayList<>();

        if (checkoutDtoRequest == null || checkoutDtoRequest.getUserId() == 0) {
            logger.info("Empty Param");
            response.setErrorMessage(FAILED_CART_LIST);
            response.setStatus(STATUS_NOT_OK);
            return response;
        }
        HashMap<Long, CheckoutShopProduct> checkoutShopProductByCartId = new HashMap<>();
        for (CheckoutShopProduct checkoutShopProduct : checkoutDtoRequest.getShopProducts()) {
            checkoutShopProductByCartId.put(checkoutShopProduct.getCartId(), checkoutShopProduct);
        }

        // get cache for comparison
        MultiCartCache multiCartCache = getByCacheId(checkoutDtoRequest.getUserId().toString());
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
            logger.info("Cart List Empty, userID = " + checkoutDtoRequest.getUserId());
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
                logger.info("Cart id not found in db");
                response.setErrorMessage(FAILED_CART_LIST);
                response.setStatus(STATUS_NOT_OK);
                return response;
            }
            cartItemFromDB.add(cartItemOptional.get());

            VwProductDetails product = vwProductDetailsService.findByProductIdProductDetails(cartItem.getProductID().intValue());
            if (product != null) {
                productMap.put(product.getProductId().longValue(), product);
            }

            Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.SHOP_STATUS_DELETED);
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
                logger.info("Product not found for product_id : " + cartItem.getProductID());
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
                if (shopGroupData.getShop().getStatus().equals(CART_SHOP_CLOSE)) {
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
                logger.info("Product not found for shop_id : " + cartDetail.getVWProductDto().getShopId());
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
                Long size = 0L;
                if (cartDetails.getVWProductDto().getSize() != null && !cartDetails.getVWProductDto().getSize().trim().equalsIgnoreCase("")) {
                    try {
                        size = Long.parseLong(cartDetails.getVWProductDto().getSize());
                    } catch (Exception ex) {
                        logger.error("error parse = " + cartDetails.getVWProductDto().getSize());
                    }
                }
                CostParent costParent = addressService.getCostByCityId(checkoutShopProduct.getOriginCityId().toString(), checkoutShopProduct.getDestincationCityId().toString(),
                        size, checkoutShopProduct.getCourier());
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
            checkoutParameterResponse.setCustomerId(checkoutDtoRequest.getUserId());
        }
        // save gross amount and get order id
        Order order = new Order();
        order.setCustomerId(checkoutDtoRequest.getUserId());
        order.setGrossAmount(checkoutParameterResponse.getGrossAmount());
        if (checkoutDtoRequest.getShopProducts().size() != 0) {
            order.setShopId(checkoutDtoRequest.getShopProducts().get(0).getShopId());
        }
        order.setStatus(ORDER_STATUS_WAITING_PAYMENT_METHOD);
        order.setQuantity(cartItemList.get(0).getQuantity().intValue());
        order.setCustAddress(Integer.valueOf(checkoutDtoRequest.getAddressId()));
        order.setShopId(cartItemList.get(0).getShopID());
        order.setCourier(checkoutDtoRequest.getShopProducts().get(0).getCourier());
        order.setService(checkoutDtoRequest.getShopProducts().get(0).getService());
        order = orderRepository.save(order);

        logger.info("order = " + order);

        for (CartItem cartItem : cartItemFromDB) {
            // update cart db
            CartItem cartItemUpdate = cartItem;
            cartItemUpdate.setStatus(CART_STATUS_CHECKOUT);
            cartItemUpdate.setTransactionID(order.getId());
            cartItemUpdate.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            // set courier, service, shop address id, user address id
            if (checkoutShopProductByCartId.containsKey(cartItemUpdate.getId())) {
                try {
                    CheckoutShopProduct checkoutShopProductData = checkoutShopProductByCartId.get(cartItemUpdate.getId());
                    cartItemUpdate.setCourier(checkoutShopProductData.getCourier());
                    cartItemUpdate.setService(checkoutShopProductData.getService());
                    cartItemUpdate.setAddressID(checkoutDtoRequest.getUserId());
                    cartItemUpdate.setShopAddressID(checkoutShopProductData.getShopAddressID());
                } catch (Exception ex) {
                    logger.error("error when update cart resi etc : " + ex.getMessage());
                }
            }

            cartItemUpdate = cartRepository.save(cartItemUpdate);
            logger.info("cartItemUpdate = " + cartItemUpdate);

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
        Order orderTop = new Order();
        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            logger.info(jsonString);
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
                    orderTop = order;
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
                    logger.info("order = " + order);
                }
            } catch (Exception ex) {
                logger.info("error : " + ex);
            }


            responseDto = new PaymentChargeDtoResponse(entity.getStatus_message(), entity.getTransaction_id(), entity.getOrder_id(), entity.getMerchant_id(),
                    grossAmount, entity.getGross_amount() + entity.getCurrency(), entity.getCurrency(), entity.getPayment_type(),
                    Utils.getCalendar().getTimeInMillis(), entity.getTransaction_status(), vaNumberDtos, entity.getFraud_status());
            List<CartItem> cartItems = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, orderTop.getCustomerId(), orderTop.getId());
            HashMap<Long, Product> productByID = new HashMap<>();
            for (CartItem item : cartItems) {
                Optional<Product> productOptional = productRepository.findById(item.getProductID().intValue());
                productOptional.ifPresent(product -> productByID.put(product.id.longValue(), product));
            }
            emailForPaymentRequest(paymentChargeRequest, responseDto, cartItems, productByID);
            return responseDto;
        }
    }

    @Override
    public PaymentChargeDtoResponse paymentCharge(PaymentChargeRequest paymentChargeRequest) throws IOException {
        // update order
        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(paymentChargeRequest.getOrderId()));
        if (!orderOptional.isPresent()) {
            logger.info("order id : " + paymentChargeRequest.getOrderId() + " is not found!");
            return new PaymentChargeDtoResponse();
        }

        String userFullName = "blank name";
        Optional<Users> userOptional = userRepository.findById(orderOptional.get().getCustomerId().intValue());
        if (userOptional.isPresent()) {
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
                .url(XENDIT_URL + XENDIT_CALLBACK_VA)
                .post(body)
                .addHeader("Authorization", XENDIT_BASIC_AUTH)  // add request headers
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            logger.info(jsonString);
            CallbackVAXendit entity = gson.fromJson(jsonString, CallbackVAXendit.class);
            String transactionTimeInMilis = Utils.getCalendar().getTimeInMillis() + "";
            Double grossAmountDouble = Double.parseDouble(entity.getExpected_amount().toString());
            Long grossAmount = grossAmountDouble.longValue();

            try {
                Order order = orderOptional.get();
                order.setGrossAmount(grossAmount);
                order.setCreatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                order.setPaymentType(entity.getBank_code());
                order.setMetadata(jsonString);
                order.setTransactionID(entity.getExternal_id());
                order.setTransactionStatus(entity.getStatus());
                order.setVaNumber(entity.getAccount_number());
                order.setStatus(ORDER_STATUS_WAITING_PAYMENT);
                order = orderRepository.save(order);
                paymentChargeRequest.setUserID(order.getCustomerId().intValue());
                logger.info("order = " + order);
            } catch (Exception ex) {
                logger.info("error pas set order : " + ex.getMessage());
            }

            List<VaNumberDto> vaNumberDtos = new ArrayList<>();
            vaNumberDtos.add(new VaNumberDto(entity.getBank_code(), entity.getAccount_number(), "", ""));
            responseDto = new PaymentChargeDtoResponse(entity.getStatus(), entity.getExternal_id(), orderOptional.get().getId() + "", "merchant_id",
                    grossAmount, entity.getExpected_amount() + "Rp.", "Rp.", "xendit",
                    Utils.getCalendar().getTimeInMillis(), entity.getStatus(), vaNumberDtos, "fraud_status");
            List<CartItem> cartItems = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, orderOptional.get().getCustomerId(), orderOptional.get().getId());
            HashMap<Long, Product> productByID = new HashMap<>();
            for (CartItem item : cartItems) {
                Optional<Product> productOptional = productRepository.findById(item.getProductID().intValue());
                productOptional.ifPresent(product -> productByID.put(product.id.longValue(), product));
            }
            emailForPaymentRequest(paymentChargeRequest, responseDto, cartItems, productByID);
            return responseDto;
        }
    }

    public PaymentChargeDtoResponse paymentCheckMidtrans(String orderId) throws IOException {
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
            logger.info(jsonString);
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


    @Override
    // check via Backend
    public PaymentChargeDtoResponse paymentCheckStatus(String orderId) throws IOException {
        PaymentChargeDtoResponse responseDto;
        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(orderId));
        if (!orderOptional.isPresent()) {
            logger.info("empty payment status for order id : " + orderId);
            return new PaymentChargeDtoResponse();
        }

        Order order = orderOptional.get();
        String transactionTimeInMilis = order.getCreatedDate().getTime() + "";
        Long grossAmount = order.getGrossAmount();
        List<VaNumberDto> vaNumberDtos = new ArrayList<>();
        vaNumberDtos.add(new VaNumberDto(order.getPaymentType(), order.getVaNumber(), "", ""));

        responseDto = new PaymentChargeDtoResponse(order.getStatus() + "", order.getTransactionID(), order.getId() + "", "xendit",
                grossAmount, grossAmount + "Rp.", "Rp.", order.getPaymentType(),
                Utils.getCalendar().getTimeInMillis(), order.getTransactionStatus(), vaNumberDtos, "fraud_status");
        if (responseDto.getTransactionStatus().equalsIgnoreCase(MIDTRANS_STATUS_SETTLEMENT)) {
            updateOrderToSettlement(orderId);
        }
        return responseDto;

    }

    public void updateOrderToSettlementMidtrans(String orderId) {
        Optional<Order> orderOptional = orderRepository.findById(Long.parseLong(orderId));
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            Timestamp transactiontime = Utils.getTimeStamp(1L);
            if (order.getTransactionTime() != null) {
                transactiontime = order.getTransactionTime();
            }
            List<CartItem> cartItems = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            HashMap<Long, Product> productByID = new HashMap<>();
            for (CartItem item : cartItems) {
                Optional<Product> productOptional = productRepository.findById(item.getProductID().intValue());
                productOptional.ifPresent(product -> productByID.put(product.id.longValue(), product));
            }
            emailForPaymentAccepted("mockBCA", transactiontime.getTime(), order.getCustomerId().intValue(), order.getGrossAmount(), cartItems, productByID);
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                order.setStatus(ORDER_STATUS_PAYMENT_SETTLED);
                order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                orderRepository.save(order);
                logger.info("Success Update to Settlement: order id : " + orderId);
            }
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
            List<CartItem> cartItems = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            HashMap<Long, Product> productByID = new HashMap<>();
            for (CartItem item : cartItems) {
                Optional<Product> productOptional = productRepository.findById(item.getProductID().intValue());
                productOptional.ifPresent(product -> productByID.put(product.id.longValue(), product));
            }
            emailForPaymentAccepted(order.getPaymentType(), transactiontime.getTime(), order.getCustomerId().intValue(), order.getGrossAmount(), cartItems, productByID);
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                order.setStatus(ORDER_STATUS_PAYMENT_SETTLED);
                order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
                orderRepository.save(order);
                logger.info("Success Update to Settlement: order id : " + orderId);
            }
        }
    }

    @Override
    public List<OrderDetailDto> orderStatusByUserID(Long userID) {
        List<OrderDetailDto> orderDetailDto = new ArrayList<>();
        List<Order> orderList = orderRepository.findByStatusIsNotAndCustomerId(0, userID);
        HashMap<Long, UserDto> mapUserByID = new HashMap<>();
        HashMap<Integer, AddressDetailDto> mapDefaultAddressDetailByShopID = new HashMap<>();
        for (Order order : orderList) {
            // if waiting for payment then check if already updated (this function can be replaced if there are cron or listener to change status from midtrans)
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                try {
                    paymentCheckStatus(order.getId().toString());
                } catch (Exception ex) {
                    logger.info("Exception check status order : " + ex.getMessage());
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
                product.setQuantity(cartItem.getQuantity());
                // get shipping detail by cart item detail
                Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.SHOP_STATUS_DELETED);
                OrderDetailDto orderResp = new OrderDetailDto();
                orderResp.setIconImg("");
                String statusTitle = getStatusTitleByStatusId(order.getStatus());
                orderResp.setOrderStatusTitle(statusTitle);
                orderResp.setOrderStatus(order.getStatus());
                orderResp.setOrderTotal(order.getGrossAmount());
                orderResp.setOrderTotalAmount(order.getQuantity().longValue());
                orderResp.setOrderID(order.getId());
                orderResp.setNotes(cartItem.getNotes());
                // get detail waybill
                if (order.getStatus().equals(ORDER_STATUS_SHIPPING)) {
                    try {
                        if (order.getId() == 1057L) {
                            logger.info("isi resi code = " + order.getResiCode() + " couriernya : " + order.getCourier());
                        }
                        String resiCode = order.getResiCode();
                        String courier = order.getCourier();
                        if (cartItem.getResiCode() != null && !cartItem.getResiCode().trim().equalsIgnoreCase("")) {
                            resiCode = cartItem.getResiCode();
                        }
                        if (cartItem.getCourier() != null && !cartItem.getCourier().trim().equalsIgnoreCase("")) {
                            courier = cartItem.getCourier();
                        }
                        orderResp.setDetailWaybill(addressService.getWaybillDetail(resiCode, courier));
                    } catch (Exception ex) {
                        logger.info("waybill error info : " + ex.getMessage());
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
                    if (mapDefaultAddressDetailByShopID.isEmpty() || !mapDefaultAddressDetailByShopID.containsKey(shopData.getId())) {
                        AddressDetailDto defaultShopDto = addressService.getShopDefaultAddress(shopData.getId());
                        mapDefaultAddressDetailByShopID.put(shopData.getId(), defaultShopDto);
                    }
                    shopData.setAddressDetailDto(mapDefaultAddressDetailByShopID.get(shopData.getId()));

                    orderResp.setShop(shopData);
                }
                orderResp = setOrderPaymentDetail(order, orderResp, cartItem);
                // check map for user detail, if not exist then fetch it from db
                if (mapUserByID.isEmpty() || !mapUserByID.containsKey(cartItem.getUserID())) {
                    UserDto userDto = getUserDetail(cartItem.getUserID());
                    mapUserByID.put(cartItem.getUserID(), userDto);
                    logger.info(userDto.toString());
                }

                orderResp.setUserDto(mapUserByID.get(cartItem.getUserID()));
                orderDetailDto.add(orderResp);
            }
        }
        return orderDetailDto;
    }

    public OrderDetailDto setOrderPaymentDetail(Order order, OrderDetailDto orderResp, CartItem cartItem) {
        orderResp.setVaNumber(order.getVaNumber());
        orderResp.setBank(order.getPaymentType());
        orderResp.setCourier(order.getCourier());
        orderResp.setService(order.getService());
        if (cartItem.getCourier() != null && !cartItem.getCourier().trim().equalsIgnoreCase("")) {
            orderResp.setCourier(cartItem.getCourier());
        }
        if (cartItem.getService() != null && !cartItem.getService().trim().equalsIgnoreCase("")) {
            orderResp.setService(cartItem.getService());
        }
        return orderResp;
    }

    public OrderDetailListProductDto setOrderListProductPaymentDetail(Order order, OrderDetailListProductDto orderResp, CartItem cartItem) {
        orderResp.setVaNumber(order.getVaNumber());
        orderResp.setBank(order.getPaymentType());
        orderResp.setCourier(order.getCourier());
        orderResp.setService(order.getService());
        if (cartItem.getCourier() != null && !cartItem.getCourier().trim().equalsIgnoreCase("")) {
            orderResp.setCourier(cartItem.getCourier());
        }
        if (cartItem.getService() != null && !cartItem.getService().trim().equalsIgnoreCase("")) {
            orderResp.setService(cartItem.getService());
        }
        return orderResp;
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
        HashMap<Integer, AddressDetailDto> mapDefaultAddressDetailByShopID = new HashMap<>();
        for (Order order : orderList) {
            // if waiting for payment then check if already updated (this function can be replaced if there are cron or listener to change status from midtrans)
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                try {
                    paymentCheckStatus(order.getId().toString());
                } catch (Exception ex) {
                    logger.info("Exception check status order : " + ex.getMessage());
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
                product.setQuantity(cartItem.getQuantity());
                // get shipping detail by cart item detail
                Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(cartItem.getShopID().intValue(), ShopServiceImpl.SHOP_STATUS_DELETED);
                OrderDetailDto orderResp = new OrderDetailDto();
                orderResp.setIconImg("");
                String statusTitle = getStatusTitleByStatusId(order.getStatus());
                orderResp.setOrderStatusTitle(statusTitle);
                orderResp.setOrderStatus(order.getStatus());
                orderResp.setOrderTotal(order.getGrossAmount());
                orderResp.setOrderTotalAmount(order.getQuantity().longValue());
                orderResp.setNotes(cartItem.getNotes());
                Timestamp orderDate = Utils.getTimeStamp(1L);
                if (order.getCreatedDate() != null) {
                    orderDate = order.getCreatedDate();
                }
                orderResp.setOrderTransactionDateString(orderDate.toString());
                orderResp.setOrderID(order.getId());
                // get detail waybill
                if (order.getStatus().equals(ORDER_STATUS_SHIPPING)) {
                    try {
                        if (order.getId() == 1057L) {
                            logger.info("isi resi code = " + order.getResiCode() + " couriernya : " + order.getCourier());
                        }
                        String resiCode = order.getResiCode();
                        String courier = order.getCourier();
                        if (cartItem.getResiCode() != null && !cartItem.getResiCode().trim().equalsIgnoreCase("")) {
                            resiCode = cartItem.getResiCode();
                        }
                        if (cartItem.getCourier() != null && !cartItem.getCourier().trim().equalsIgnoreCase("")) {
                            courier = cartItem.getCourier();
                        }
                        orderResp.setDetailWaybill(addressService.getWaybillDetail(resiCode, courier));
                    } catch (Exception ex) {
                        logger.info("waybill error info : " + ex.getMessage());
                    }
                }
                orderResp.setProduct(product);
                if (shop.isPresent()) {
                    Shop shopData = shop.get();
                    if (mapDefaultAddressDetailByShopID.isEmpty() || !mapDefaultAddressDetailByShopID.containsKey(shopData.getId())) {
                        AddressDetailDto defaultShopDto = addressService.getShopDefaultAddress(shopData.getId());
                        mapDefaultAddressDetailByShopID.put(shopData.getId(), defaultShopDto);
                    }
                    shopData.setAddressDetailDto(mapDefaultAddressDetailByShopID.get(shopData.getId()));

                    orderResp.setShop(shopData);
                }

                // check map for user detail, if not exist then fetch it from db
                if (mapUserByID.isEmpty() || !mapUserByID.containsKey(cartItem.getUserID())) {
                    UserDto userDto = getUserDetail(cartItem.getUserID());
                    mapUserByID.put(cartItem.getUserID(), userDto);
                    logger.info(userDto.toString());
                }

                orderResp.setUserDto(mapUserByID.get(cartItem.getUserID()));
                orderResp = setOrderPaymentDetail(order, orderResp, cartItem);
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
        AddressDetailDto userAddress = addressService.getDefaultAddressByRelationIdAndType(userDto.getId(), AddressServiceImpl.TYPE_USER);
        userDto.setUserAddress(userAddress);

        // get shop by user id
        Shop shop = shopService.getShopObjectByUserID(userDto.getId());
        // get shop default address
        AddressDetailDto shopAddress = addressService.getDefaultAddressByRelationIdAndType(shop.getId(), AddressServiceImpl.TYPE_SHOP);
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
            logger.info("status not found");
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

            // update resi code by cart id
            List<CartItem> cartItemList = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            for (CartItem cartItem : cartItemList) {
                if (cartItem.getShopID().equals(request.getShopID())) {
                    cartItem.setResiCode(request.getResiCode());
                    try {
                        cartRepository.save(cartItem);
                    } catch (Exception ex) {
                        logger.error("error when save cart resi = " + ex.getMessage());
                    }
                }
            }
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
                HashMap<Long, Product> productByID = new HashMap<>();
                product.ifPresent(value -> productByID.put(cartItem.getProductID(), value));
                product.ifPresent(value -> emailForOrderDone(order.getCustomerId().intValue(), value.getName(), cartItem.getProductID(), cartItems, productByID));
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
            saveCartParam.setPrice(product.get().getPrice().longValue());
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
        logger.info("insertMultiData = " + insertCache);
        response.setMessage(SUCCESS_UPDATE_CART);
        response.setStatus(STATUS_OK);
        return response;
    }

    public void emailForPaymentRequest(PaymentChargeRequest paymentChargeRequest, PaymentChargeDtoResponse paymentChargeDtoResponse,
                                       List<CartItem> cartItems, HashMap<Long, Product> productByID) {
        StringBuilder rincianPesanan = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            String productName = productByID.get(cartItem.getProductID()) == null ? cartItem.getProductID() + "" : productByID.get(cartItem.getProductID()).getName();
            rincianPesanan.append("Produk : ").append(productName).append(", Qty : ").append(cartItem.getQuantity()).append(", Harga : ").append(cartItem.getPrice()).append(" \n");
        }
        Optional<Users> user = userRepository.findById(paymentChargeRequest.getUserID());

        Calendar calendar = Utils.getCalendar();
        calendar.setTimeInMillis(paymentChargeDtoResponse.getTransactionTimeInMilis());
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        String dateTimeString = "2008-01-01 13:30:10";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateTimeString = sdf.format(calendar.getTime());
        if (user.isPresent()) {
            String messageBodyRegister = "Halo! Terimakasih telah melakukan transaksi checkout\n" +
                    "Silahkan lakukan pembayaran " + paymentChargeRequest.getBank().toUpperCase() + " dengan detail sebagai berikut :\n" +
                    "\n" +
                    "Total Bayar : Rp." + paymentChargeDtoResponse.getGrossAmount() + "\n" +
                    "\n" +
                    "Metode Pembayaran : " + paymentChargeRequest.getBank().toUpperCase() + "\n" +
                    "\n" +
                    "Kode Virtual Account : " + paymentChargeDtoResponse.getVaNumberDto().get(0).getVaNumber() + "\n" +
                    "\n" +
                    "Rincian Pesanan : \n" +
                    "\n" +
                    rincianPesanan +
                    "\n" +
                    "Mohon melakukan pembayaran sebelum :" + dateTimeString + " \n" +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n";
            String messageSubjectRegister = "Menunggu Pembayaran " + paymentChargeRequest.getBank().toUpperCase() + " untuk pembayaran dengan order id " + paymentChargeRequest.getOrderId();
            sentEmail(user.get().getEmail(), messageBodyRegister, messageSubjectRegister);
        } else {
            logger.info("Username/Email tidak ditemukan");
        }
    }

    public void emailForPaymentAccepted(String kodeBank, Long tanggalBayar, Integer userID, Long totalBayar,
                                        List<CartItem> cartItems, HashMap<Long, Product> productByID) {
        StringBuilder rincianPesanan = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            String productName = productByID.get(cartItem.getProductID()) == null ? cartItem.getProductID() + "" : productByID.get(cartItem.getProductID()).getName();
            rincianPesanan.append("Produk : ").append(productName).append(", Qty : ").append(cartItem.getQuantity()).append(", Harga : ").append(cartItem.getPrice()).append(" \n");
        }
        Calendar calendar = Utils.getCalendar();
        calendar.setTimeInMillis(tanggalBayar);
        calendar.add(Calendar.DATE, 1);
        calendar.add(Calendar.HOUR_OF_DAY, 7);
        String dateTimeString = "2008-01-01 13:30:10";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateTimeString = sdf.format(calendar.getTime());
        Optional<Users> user = userRepository.findById(userID);
        if (user.isPresent()) {
            String messageBodyRegister = "Halo! Pembayaran terverifikasi dan pesanan telah diteruskan ke penjual \n" +
                    "Terima kasih, ya! Berikut detail pembayaranmu :\n" +
                    "\n" +
                    "Total Bayar : Rp." + totalBayar + "\n" +
                    "\n" +
                    "Metode Pembayaran : " + kodeBank.toUpperCase() + "\n" +
                    "\n" +
                    "Waktu Pembayaran : " + dateTimeString + "\n" +
                    "\n" +
                    "Rincian Pesanan : \n" +
                    "\n" +
                    rincianPesanan +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n";
            String messageSubjectRegister = "Checkout Pesanan dengan " + kodeBank.toUpperCase() + " Berhasil tanggal " + dateTimeString;
            sentEmail(user.get().getEmail(), messageBodyRegister, messageSubjectRegister);
        } else {
            logger.info("Username/Email tidak ditemukan");
        }
    }

    public void emailForOrderDone(Integer userID, String productName, Long productID, List<CartItem> cartItems, HashMap<Long, Product> productByID) {
        StringBuilder rincianPesanan = new StringBuilder();
        for (CartItem cartItem : cartItems) {
            if (!cartItem.getProductID().equals(productID)) {
                continue;
            }
            String productNameDetail = productByID.get(cartItem.getProductID()) == null ? cartItem.getProductID() + "" : productByID.get(cartItem.getProductID()).getName();
            rincianPesanan.append("Produk : ").append(productNameDetail).append(", Qty : ").append(cartItem.getQuantity()).append(", Harga : ").append(cartItem.getPrice()).append(" \n");
            break;
        }
        Optional<Users> user = userRepository.findById(userID);
        if (user.isPresent()) {
            String messageBodyRegister = "Halo! Yeay barangmu sudah sampai! \n" +
                    "Terimakasih sudah berbelanja dan mendukung para penjual di JakTani. \n" +
                    "\n" +
                    "Rincian Pesanan : \n" +
                    "\n" +
                    rincianPesanan +
                    "\n" +
                    "Terima Kasih,\n" +
                    "Team Jak Tani\n" +
                    "\n";
            String messageSubjectRegister = "Pesanan Selesai: " + productName;
            sentEmail(user.get().getEmail(), messageBodyRegister, messageSubjectRegister);
        } else {
            logger.info("Username/Email tidak ditemukan");
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
            logger.info("Error caught : " + e.getMessage());
        }

    }

    public Order verifyCallBackFVA(CallbackFVA callbackFVA) {
        Order order = new Order();
        // get order by order id and update the status
        Optional<Order> optionalOrder = orderRepository.findById(Long.parseLong(callbackFVA.getExternal_id()));
        if (!optionalOrder.isPresent()) {
            logger.info("empty order id for : " + callbackFVA.getExternal_id());
            return order;
        }
        order = optionalOrder.get();
        if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
            order.setStatus(ORDER_STATUS_PAYMENT_SETTLED);
            order.setUpdatedDate(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
            order.setTransactionID(callbackFVA.getPayment_id());
            order.setTransactionStatus(MIDTRANS_STATUS_SETTLEMENT);
            orderRepository.save(order);
            logger.info("Success Update to Settlement: order id : " + order.getId());
            List<CartItem> cartItems = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            HashMap<Long, Product> productByID = new HashMap<>();
            for (CartItem item : cartItems) {
                Optional<Product> productOptional = productRepository.findById(item.getProductID().intValue());
                productOptional.ifPresent(product -> productByID.put(product.id.longValue(), product));
            }
            emailForPaymentAccepted(callbackFVA.getBank_code(), Utils.getCalendar().getTimeInMillis(), order.getCustomerId().intValue(), order.getGrossAmount(), cartItems, productByID);
        }
        // after received need to verify it to xendit
        verifyCallbackXendit(callbackFVA);
        return order;
    }

    public void verifyCallbackXendit(CallbackFVA callbackFVA) {
        String urlHitVerifyXendit = XENDIT_URL + XENDIT_VERIFY_CALLBACK_VA;
        urlHitVerifyXendit = urlHitVerifyXendit.replace("{{payment_id}}", callbackFVA.getPayment_id());
        Request request = new Request.Builder()
                .url(urlHitVerifyXendit)
                .addHeader("Authorization", XENDIT_BASIC_AUTH)  // add request headers
                .addHeader("content-type", "application/json")
                .build();
        logger.info("url xendit : " + urlHitVerifyXendit);

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            logger.info("success callback verify");
            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            logger.info(jsonString);
            CallbackVerifyFVA entity = gson.fromJson(jsonString, CallbackVerifyFVA.class);
            logger.info(entity.toString());
        } catch (Exception ex) {
            logger.info("error when execute verify payment");
        }
    }

    public SimulatePaymentFVA simulatePaymentFVA(Long transferAmount, String orderID) {
        SimulatePaymentFVA resp = new SimulatePaymentFVA();
        String json = "{\n" +
                "    \"amount\": \"{{transferAmount}}\"\n" +
                "}";
        json = json.replace("{{transferAmount}}", transferAmount + "");
        RequestBody body = RequestBody.create(JSON, json);
        String urlSimulatePayment = XENDIT_URL + XENDIT_FVA_SIMULATE_PAYMENT;
        urlSimulatePayment = urlSimulatePayment.replace("{external_id}", orderID);
        logger.info("debug url simulate payment = " + urlSimulatePayment);
        logger.info("info url simulate payment = " + urlSimulatePayment);
        logger.error("error url simulate payment = " + urlSimulatePayment);
        Request request = new Request.Builder()
                .url(urlSimulatePayment)
                .post(body)
                .addHeader("Authorization", XENDIT_BASIC_AUTH)  // add request headers
                .addHeader("content-type", "application/json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            logger.info(jsonString);
            return gson.fromJson(jsonString, SimulatePaymentFVA.class);
        } catch (Exception ex) {
            logger.info("failed simulate payment for order id = " + orderID);
        }
        return resp;
    }

    @Override
    public OrderDetailListProductDto invoiceByOrderID(Long orderID) {
        OrderDetailListProductDto orderDetailDto = new OrderDetailListProductDto();
        Optional<CartItem> cartItemList = cartRepository.findFirstByStatusIsNotAndTransactionID(CART_STATUS_DELETED, orderID);
        if (!cartItemList.isPresent()) {
            logger.info("invoiceByOrderID, empty for order id : " + orderID);
            return orderDetailDto;
        }
        Optional<Order> orderOptional = orderRepository.findById(orderID);
        HashMap<Long, UserDto> mapUserByID = new HashMap<>();
        HashMap<Integer, AddressDetailDto> mapDefaultAddressDetailByShopID = new HashMap<>();
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            // if waiting for payment then check if already updated (this function can be replaced if there are cron or listener to change status from midtrans)
            if (order.getStatus().equals(ORDER_STATUS_WAITING_PAYMENT)) {
                try {
                    paymentCheckStatus(order.getId().toString());
                } catch (Exception ex) {
                    logger.info("Exception check status order : " + ex.getMessage());
                }
                Optional<Order> orderChanged = orderRepository.findById(order.getId());
                if (orderChanged.isPresent()) {
                    order = orderChanged.get();
                }
            }
            // get product id by order id from cart
            List<CartItem> cartItemOptional = cartRepository.findByStatusAndUserIDAndTransactionID(CART_STATUS_CHECKOUT, order.getCustomerId(), order.getId());
            // get product detail by product id

            List<VwProductDetails> productDetails = new ArrayList<>();
            String resiCode = "";
            String courier = "";
            Long shopID = 0L;
            Long userID = 0L;
            CartItem cartItemOne = new CartItem();
            for (CartItem cartItem : cartItemOptional) {
                // get shop detail by product shop id
                VwProductDetails product = vwProductDetailsService.findByProductIdProductDetails(cartItem.getProductID().intValue());
                product.setQuantity(cartItem.getQuantity());
                product.setNotes(cartItem.getNotes());
                productDetails.add(product);
                resiCode = cartItem.getResiCode();
                courier = cartItem.getCourier();
                shopID = cartItem.getShopID();
                userID = cartItem.getUserID();
                cartItemOne = cartItem;
            }
            OrderDetailListProductDto orderResp = new OrderDetailListProductDto();
            orderResp.setProduct(productDetails);
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
            // get detail waybill
            if (order.getStatus().equals(ORDER_STATUS_SHIPPING)) {
                try {
                    orderResp.setDetailWaybill(addressService.getWaybillDetail(resiCode, courier));
                } catch (Exception ex) {
                    logger.info("waybill error info : " + ex.getMessage());
                }
            }
            // get shipping detail by cart item detail
            Optional<Shop> shop = shopRepository.findByIdAndStatusIsNot(shopID.intValue(), ShopServiceImpl.SHOP_STATUS_DELETED);
            if (shop.isPresent()) {
                Shop shopData = shop.get();
                AddressDetailDto defaultShopDto = addressService.getShopDefaultAddress(shopData.getId());
                mapDefaultAddressDetailByShopID.put(shopData.getId(), defaultShopDto);
                shopData.setAddressDetailDto(mapDefaultAddressDetailByShopID.get(shopData.getId()));

                orderResp.setShop(shopData);
            }

            // check map for user detail, if not exist then fetch it from db
            UserDto userDto = getUserDetail(userID);
            mapUserByID.put(userID, userDto);
            logger.info(userDto.toString());

            orderResp.setUserDto(mapUserByID.get(userID));
            orderResp = setOrderListProductPaymentDetail(order, orderResp, cartItemOne);
            return orderResp;
        }
        return orderDetailDto;
    }
}
