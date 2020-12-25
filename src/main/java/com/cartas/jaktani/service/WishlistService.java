package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.WishlistDto;

public interface WishlistService {
	Object getWishlistByID(Integer id);
	Object saveWishlist(WishlistDto wishlistDto);
	Object getAllWishlistByUserId(Integer userId);
	Object getAllWishlist();
}
