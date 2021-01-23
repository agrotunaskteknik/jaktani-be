package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;

public interface CartService {
    AddToCartDtoResponse addToCart(AddToCartDtoRequest addToCartDtoRequest);
    AddToCartDtoResponse getCounter(Long userID);
    CommonResponse removeCart(RemoveCartDto removeCartDto);
    CommonResponse updateCart(AddToCartDtoRequest addToCartDtoRequest);
    CartListResponse cartList(CartListDtoRequest cartListDtoRequest);
}
