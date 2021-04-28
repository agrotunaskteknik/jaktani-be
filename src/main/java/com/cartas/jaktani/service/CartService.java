package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;

import java.io.IOException;
import java.util.List;

public interface CartService {
    AddToCartDtoResponse addToCart(AddToCartDtoRequest addToCartDtoRequest);

    AddToCartDtoResponse getCounter(Long userID);

    CommonResponse removeCart(RemoveCartDto removeCartDto);

    CommonResponse updateCart(AddToCartDtoRequest addToCartDtoRequest);

    CommonResponse updateCartV2(List<AddToCartDtoRequest> addToCartDtoRequest);

    CartListResponse cartList(CartListDtoRequest cartListDtoRequest);

    SAFDtoResponse shipmentAddressForm(CartListDtoRequest cartListDtoRequest);

    CheckoutDtoResponse checkout(CheckoutDtoRequest cartListDtoRequest) throws IOException;

    PaymentChargeDtoResponse paymentCharge(PaymentChargeRequest paymentChargeRequest) throws IOException;

    PaymentChargeDtoResponse paymentCheckStatus(String orderId) throws IOException;

    List<OrderDetailDto> orderStatusByUserID(Long userID);

    List<OrderDetailDto> orderStatusByShopID(Long shopID);

    public void sellerVerifyOrder(VerifyOrderShippingRequest request);

    public void sellerVerifyOrderShipping(VerifyOrderShippingRequest request);

    public void sellerVerifyOrderSent(VerifyOrderShippingRequest request);

    public void sellerVerifyReview(VerifyOrderShippingRequest request);
}
