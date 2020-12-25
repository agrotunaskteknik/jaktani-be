package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.RatingDto;

public interface RatingService {
	Object getRatingByID(Integer userID);
	Object getAllRatings();
    Object deleteRatingByID(Integer id);
    Object saveRating(RatingDto rating);
    Object getAllRatingByProductId(Integer productId);
    Object getAllRatingByShopId(Integer shopId);
    Object getRatingByCreatedByAndProductId(Integer createdBy, Integer productId);
}
