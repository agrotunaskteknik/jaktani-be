package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ProductDto;

public interface ProductService {
	Object getProductByID(Integer id);
	Object getProductByName(String name);
	Object getAllProducts();
    Object deleteProductByID(Integer id);
    Object addProduct(ProductDto product);
    Object editProduct(ProductDto product);
    Object getAllProductByShopId(Integer shopId);
}
