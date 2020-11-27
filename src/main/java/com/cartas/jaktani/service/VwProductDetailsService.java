package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ParamRequestDto;

public interface VwProductDetailsService {
	Object findByProductId(Integer productId);
	Object allProductTypeByProductId(Integer productId);
	Object findAllByShopId(Integer shopId);
	Object searchAllProduct(ParamRequestDto paramRequestDto);
}
