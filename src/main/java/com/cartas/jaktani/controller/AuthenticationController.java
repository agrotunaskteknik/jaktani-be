package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.Student;
import com.cartas.jaktani.repository.StudentRepository;
import com.cartas.jaktani.service.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);


    @Autowired
    VwProductDetailsService vwProductDetailsService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    AddressService addressService;
    @Autowired
    CartService cartService;

    Integer grade = 1;
    public static String staticKey = "Eng000";
    @Autowired
    StudentRepository studentRepository;

    List<CallbackFVA> callbackFVAS = new ArrayList<>();

    @PostMapping(path = "/callback_fva")
    public Object callbackFVA(@RequestBody CallbackFVA callbackFVA) {
        logger.info("called callback_fva info");
        if (null == callbackFVAS || callbackFVAS.size() == 0) {
            callbackFVAS = new ArrayList<>();
        }
        callbackFVAS.add(callbackFVA);

        cartService.verifyCallBackFVA(callbackFVA);

        return callbackFVAS;
    }

    @PostMapping(path = "/callback_fva_paid")
    public Object callbackFVAPaid(@RequestBody CallbackFVA callbackFVA) {
        logger.info("called callback_fva_paid info");
        if (null == callbackFVAS || callbackFVAS.size() == 0) {
            callbackFVAS = new ArrayList<>();
        }
        callbackFVA.setStatus(CartServiceImpl.XENDIT_PAYMENT_COMPLETED);
        callbackFVAS.add(callbackFVA);

        cartService.verifyCallBackFVA(callbackFVA);

        return callbackFVAS;
    }

    @GetMapping(path = "/list_callback_fva")
    public Object listCallbackFVA() {
        return callbackFVAS;
    }

    @GetMapping(path = "/simulate_payment")
    public SimulatePaymentFVA simulatePayment(@RequestParam(value = "transfer_amount") Long transferAmount,
                                              @RequestParam(value = "order_id") String orderID) {
        return cartService.simulatePaymentFVA(transferAmount, orderID);
    }

    @GetMapping(path = "/student/insert_data")
    public String insertDatas() {
        Student student = new Student(
                staticKey + grade, "John Doe " + grade, Student.Gender.MALE, grade);
        studentRepository.save(student);
        grade++;
        return student.toString();
    }

    @GetMapping(path = "/student/get_all_data")
    public String getAllDatas() {
        List<Student> students = new ArrayList<>();
        studentRepository.findAll().forEach(students::add);
        return students.toString();
    }

    @GetMapping(path = "/student/get_data_by_key")
    public String getAllDatas(@RequestParam(value = "id") String id) {
        System.out.println(staticKey + id);
        Student retrievedStudent = studentRepository.findById(staticKey + id).get();
        return retrievedStudent.toString();
    }

    @GetMapping(path = "/logistic/get_provinces")
    public ProvinceParent getProvinces() throws IOException {
        return addressService.getProvinces();
    }

    @GetMapping(path = "/logistic/get_cities")
    public CityParent getCities(@RequestParam(value = "id") String id) throws IOException {
        return addressService.getCitiesByProvinceId(id);
    }

    @GetMapping(path = "/logistic/rates")
    public CostParent getRates(@RequestParam(value = "origin_city_id") String originCityId,
                               @RequestParam(value = "destination_city_id") String destinationCityId,
                               @RequestParam(value = "weight") Long weight,
                               @RequestParam(value = "courier") String courier) throws IOException {
        return addressService.getCostByCityId(originCityId, destinationCityId, weight, courier);
    }

    @GetMapping(path = "/logistic/detail_waybill")
    public RajaOngkirWaybillResponseDto getDetailWaybill(@RequestParam(value = "waybill") String waybill,
                                                         @RequestParam(value = "courier") String courier) throws IOException {
        return addressService.getWaybillDetail(waybill, courier);
    }

    @PostMapping(path = "/searchAllProduct")
    public Object searchAllProduct(@RequestBody ParamRequestDto paramRequestDto) {
        return vwProductDetailsService.searchAllProduct(paramRequestDto);
    }

    @GetMapping(path = "/findProductById/{productId}")
    public Object findProductById(@PathVariable(name = "productId") Integer productId) {
        return vwProductDetailsService.findByProductId(productId);
    }

    @GetMapping(path = "/allCategory")
    public Object getAllCategory() {
        return categoryService.getAllCategoryName();
    }

    @GetMapping(path = "/allProductTypeByProductId/{productId}")
    public Object allProductTypeByProductId(@PathVariable(name = "productId") Integer productId) {
        return vwProductDetailsService.allProductTypeByProductId(productId);
    }

    @RequestMapping(value = "/payment/charge", method = RequestMethod.POST)
    public ResponseEntity<?> paymentCharge(@RequestBody PaymentChargeRequest paymentChargeRequest) {
        try {
            PaymentChargeDtoResponse response = cartService.paymentCharge(paymentChargeRequest);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("paymentCharge Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/payment/status", method = RequestMethod.GET)
    public ResponseEntity<?> paymentStatus(@RequestParam(value = "order_id") String orderId) {
        try {
            PaymentChargeDtoResponse response = cartService.paymentCheckStatus(orderId);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("paymentStatus Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/order-history/list", method = RequestMethod.GET)
    public ResponseEntity<?> orderTransactionList(@RequestParam(value = "user_id") Long userID) {
        try {
            List<OrderDetailDto> response = cartService.orderStatusByUserID(userID);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("orderTransactionList Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/order-list", method = RequestMethod.GET)
    public ResponseEntity<?> shopOrderList(@RequestParam(value = "shop_id") Long shopID) {
        try {
            List<OrderDetailDto> response = cartService.orderStatusByShopID(shopID);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("shopOrderList Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/verify_order", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyOrder(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyOrder(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyOrder Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/verify_order_shipping", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyOrderShipping(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getResiCode().trim().equalsIgnoreCase("")) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty ResiCode", "NOT_OK", "")));
            }
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyOrderShipping(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyOrderShipping Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/invoice-by-order-id", method = RequestMethod.GET)
    public ResponseEntity<?> invoiceByOrderID(@RequestParam(value = "order_id") Long orderID) {
        try {
            OrderDetailListProductDto response = cartService.invoiceByOrderID(orderID);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.info("invoiceByOrderID Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/buyer/verify_order_sent", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyOrderSent(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyOrderSent(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyOrderSent Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/buyer/verify_review", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyReview(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyReview(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyReview Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @GetMapping(path = "/payment/get_all")
    public List<PaymentGatewayDto> getAllPaymentType() {
        List<PaymentGatewayDto> paymentGatewayDtos = new ArrayList<>();
        PaymentGatewayDetail paymentGatewayDetailBCA = new PaymentGatewayDetail("bca");
        PaymentGatewayDetail paymentGatewayDetailBNI = new PaymentGatewayDetail("bni");
        PaymentGatewayDetail paymentGatewayDetailBRI = new PaymentGatewayDetail("bri");
        PaymentGatewayDetail paymentGatewayDetailMandiri = new PaymentGatewayDetail("mandiri");
        PaymentGatewayDetail paymentGatewayDetailPermata = new PaymentGatewayDetail("permata");
        String paymentType = "bank_transfer";
        PaymentGatewayDto paymentGatewayDtoMandiri = new PaymentGatewayDto(paymentType, "mandiri_image", "Deskripsi pembayaran dengan Virtual Account Mandiri",
                "Virtual Account Mandiri", paymentGatewayDetailMandiri);
        PaymentGatewayDto paymentGatewayDtoBNI = new PaymentGatewayDto(paymentType, "bni_image", "Deskripsi pembayaran dengan Virtual Account BNI",
                "Virtual Account BNI", paymentGatewayDetailBNI);
        PaymentGatewayDto paymentGatewayDtoBRI = new PaymentGatewayDto(paymentType, "bri_image", "Deskripsi pembayaran dengan Virtual Account BRI",
                "Virtual Account BRI", paymentGatewayDetailBRI);
        PaymentGatewayDto paymentGatewayDtoPermata = new PaymentGatewayDto(paymentType, "permata_image", "Deskripsi pembayaran dengan Virtual Account BRI",
                "Virtual Account Permata", paymentGatewayDetailPermata);
        PaymentGatewayDto paymentGatewayDtoBCA = new PaymentGatewayDto(paymentType, "bca_image", "Deskripsi pembayaran dengan Virtual Account BCA",
                "Virtual Account BCA", paymentGatewayDetailBCA);

        paymentGatewayDtos.add(paymentGatewayDtoBCA);
        paymentGatewayDtos.add(paymentGatewayDtoBNI);
        paymentGatewayDtos.add(paymentGatewayDtoBRI);
        paymentGatewayDtos.add(paymentGatewayDtoMandiri);
        paymentGatewayDtos.add(paymentGatewayDtoPermata);
        return paymentGatewayDtos;
    }

    @GetMapping(path = "/logistic/get_all_courier")
    public List<CourierDetailDto> getAllCourier() {
        List<CourierDetailDto> courierDetailDtos = new ArrayList<>();
        courierDetailDtos.add(new CourierDetailDto("jne_image", "JNE", "jne", "Pengiriman menggunakan JNE"));
        courierDetailDtos.add(new CourierDetailDto("pos_image", "POS", "pos", "Pengiriman menggunakan POS"));
        courierDetailDtos.add(new CourierDetailDto("tiki_image", "TIKI", "tiki", "Pengiriman menggunakan TIKI"));
        courierDetailDtos.add(new CourierDetailDto("rpx_image", "RPX Holding", "rpx", "Pengiriman menggunakan RPX Holding"));
        courierDetailDtos.add(new CourierDetailDto("pandu_image", "PANDU Logistic", "pandu", "Pengiriman menggunakan PANDU Logistic"));
        courierDetailDtos.add(new CourierDetailDto("wahana_image", "WAHANA", "wahana", "Pengiriman menggunakan WAHANA"));
        courierDetailDtos.add(new CourierDetailDto("sicepat_image", "SiCepat Express", "sicepat", "Pengiriman menggunakan SiCepat Express"));
        courierDetailDtos.add(new CourierDetailDto("jnt_image", "J&T Express", "jnt", "Pengiriman menggunakan J&T Express"));
        courierDetailDtos.add(new CourierDetailDto("pahala_image", "Pahala Kencana Express", "pahala", "Pengiriman menggunakan Pahala Kencana Express"));
        courierDetailDtos.add(new CourierDetailDto("sap_image", "SAP Express", "sap", "Pengiriman menggunakan SAP Express"));
        courierDetailDtos.add(new CourierDetailDto("jet_image", "JET Express", "jet", "Pengiriman menggunakan JET Express"));
        courierDetailDtos.add(new CourierDetailDto("indah_image", "Indah Kargo", "indah", "Pengiriman menggunakan Indah Kargo"));
        courierDetailDtos.add(new CourierDetailDto("dse_image", "21 Express", "dse", "Pengiriman menggunakan 21 Express"));
        courierDetailDtos.add(new CourierDetailDto("slis_image", "Solusi Ekspres", "slis", "Pengiriman menggunakan Solusi Ekspres"));
        courierDetailDtos.add(new CourierDetailDto("first_image", "First Logistics", "first", "Pengiriman menggunakan First Logistics"));
        courierDetailDtos.add(new CourierDetailDto("ncs_image", "Nusantara Card Semesta", "ncs", "Pengiriman menggunakan Nusantara Card Semesta"));
        courierDetailDtos.add(new CourierDetailDto("star_image", "Star Cargo", "star", "Pengiriman menggunakan Star Cargo"));
        courierDetailDtos.add(new CourierDetailDto("ninja_image", "Ninja Xpress", "ninja", "Pengiriman menggunakan Ninja Xpress"));
        courierDetailDtos.add(new CourierDetailDto("lion_image", "Lion Parcel", "lion", "Pengiriman menggunakan Lion Parcel"));
        courierDetailDtos.add(new CourierDetailDto("idl_image", "IDL", "idl", "Pengiriman menggunakan IDL"));
        courierDetailDtos.add(new CourierDetailDto("rex_image", "Royal Express Indonesia", "rex", "Pengiriman menggunakan Royal Express Indonesia"));
        courierDetailDtos.add(new CourierDetailDto("ide_image", "ID Express", "ide", "Pengiriman menggunakan ID Express"));
        courierDetailDtos.add(new CourierDetailDto("sentral_image", "Sentral Cargo", "sentral", "Pengiriman menggunakan Sentral Cargo"));
        courierDetailDtos.add(new CourierDetailDto("anteraja_image", "AnterAja", "anteraja", "Pengiriman menggunakan AnterAja"));

        return courierDetailDtos;
    }

    @GetMapping(path = "/id/{address_id}")
    public Object getByAddressId(@PathVariable(name = "address_id") Integer addressId) {
        return addressService.getByAddressId(addressId);
    }

    @GetMapping(path = "/all/shop/{shop_id}")
    public Object getAllShop(@PathVariable(name = "shop_id") Integer shopId) {
        return addressService.getAllShopAddresses(shopId);
    }

    @GetMapping(path = "/all/user/{user_id}")
    public Object getAllUser(@PathVariable(name = "user_id") Integer userId) {
        return addressService.getAllUserAddresses(userId);
    }


    @PostMapping(path = "/add/user")
    public Object addUserAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        return addressService.saveAddress(addressDetailDto);
    }

    @PostMapping(path = "/add/shop")
    public Object addShopAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        return addressService.saveAddress(addressDetailDto);
    }

    @PostMapping(path = "/edit/shop")
    public Object editShopAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        return addressService.updateAddress(addressDetailDto);
    }

    @PostMapping(path = "/edit/user")
    public Object editUserAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        return addressService.updateAddress(addressDetailDto);
    }

}
