package com.cartas.jaktani.service;

public interface VwProductDetailsService {
	Object findByProductId(Integer productId);
	Object findAllByShopId(Integer shopId);
}
