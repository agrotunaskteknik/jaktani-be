package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ShopDto;

public interface ShopService {
	Object getShopByID(Integer userID);
	Object getShopByName(String name);
	Object getAllShops();
    Object deleteShopByID(Integer id);
    Object addShop(ShopDto shop);
    
}
