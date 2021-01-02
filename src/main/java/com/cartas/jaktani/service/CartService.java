package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;

public interface CartService {
    CommonResponse addToCart(AddToCartDtoRequest addToCartDtoRequest);
    CommonResponse removeCart(RemoveCartDto removeCartDto);
    CommonResponse updateCart(AddToCartDtoRequest addToCartDtoRequest);
    CartListResponse cartList(CartListDtoRequest cartListDtoRequest);
}
