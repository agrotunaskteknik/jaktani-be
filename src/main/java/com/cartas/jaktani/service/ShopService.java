package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ShopDto;

public interface ShopService {
	Object getShopByID(Integer id);
	Object getShopByUserID(Integer userID);
	Object getShopByName(String name);
	Object getAllShops();
	Object getAllShopsByStatus(Integer status);
    Object deleteShopByID(Integer id);
    Object updateShopStatusByID(Integer id, Integer status);
    Object addShop(ShopDto shop);
    Object editShop(ShopDto shop);
    byte[] getLogoFile(String urlPath);
}
