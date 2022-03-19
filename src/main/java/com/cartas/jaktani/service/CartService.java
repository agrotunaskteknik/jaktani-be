package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.Order;

import java.io.IOException;
import java.util.List;

public interface CartService {
    AddToCartDtoResponse addToCart(AddToCartDtoRequest addToCartDtoRequest);

    AddToCartDtoResponse getCounter(Long userID);

    CommonResponse removeCart(RemoveCartDto removeCartDto);

    CommonResponse updateCart(AddToCartDtoRequest addToCartDtoRequest);

    CommonResponse updateCartV2(List<AddToCartDtoRequest> addToCartDtoRequest);

    Order verifyCallBackFVA(CallbackFVA callbackFVA);

    SimulatePaymentFVA simulatePaymentFVA(Long transferAmount, String orderID);

    CartListResponse cartList(CartListDtoRequest cartListDtoRequest);

    SAFDtoResponse shipmentAddressForm(CartListDtoRequest cartListDtoRequest);

    CheckoutDtoResponse checkout(CheckoutDtoRequest cartListDtoRequest) throws IOException;

    PaymentChargeDtoResponse paymentCharge(PaymentChargeRequest paymentChargeRequest) throws IOException;

    PaymentChargeDtoResponse paymentCheckStatus(String orderId) throws IOException;

    List<OrderDetailDto> orderStatusByUserID(Long userID);

    List<OrderDetailDto> orderStatusByShopID(Long shopID);

    void sellerVerifyOrder(VerifyOrderShippingRequest request);

    List<VerifyOrderShipping> sellerVerifyOrderShipping(VerifyOrderShippingRequest request);

    void sellerVerifyOrderSent(VerifyOrderShippingRequest request);

    void sellerVerifyReview(VerifyOrderShippingRequest request);

    OrderDetailListProductDto invoiceByOrderID(Long orderID);
}
