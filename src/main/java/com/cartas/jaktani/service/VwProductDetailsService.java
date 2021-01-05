package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ParamRequestDto;
import com.cartas.jaktani.model.VwProductDetails;

public interface VwProductDetailsService {
    Object findByProductId(Integer productId);

    Object allProductTypeByProductId(Integer productId);

    Object findAllByShopId(Integer shopId);

    Object searchAllProduct(ParamRequestDto paramRequestDto);

    VwProductDetails findByProductIdProductDetails(Integer productId);
}
