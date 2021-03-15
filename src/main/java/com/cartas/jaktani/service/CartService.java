package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;

import java.io.IOException;

public interface CartService {
    AddToCartDtoResponse addToCart(AddToCartDtoRequest addToCartDtoRequest);

    AddToCartDtoResponse getCounter(Long userID);

    CommonResponse removeCart(RemoveCartDto removeCartDto);

    CommonResponse updateCart(AddToCartDtoRequest addToCartDtoRequest);

    CartListResponse cartList(CartListDtoRequest cartListDtoRequest);

    SAFDtoResponse shipmentAddressForm(CartListDtoRequest cartListDtoRequest);

    CheckoutDtoResponse checkout(CheckoutDtoRequest cartListDtoRequest) throws IOException;

    PaymentChargeDtoResponse paymentCharge(PaymentChargeRequest paymentChargeRequest) throws IOException;

    PaymentChargeDtoResponse paymentCheckStatus(String orderId) throws IOException;
}
