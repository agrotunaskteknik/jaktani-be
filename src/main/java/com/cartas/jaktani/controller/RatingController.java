package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.RatingDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/rating")
public class RatingController {
    @Autowired
    RatingService ratingService;

    @GetMapping(path = "/getById/{id}")
    public Object getRatingByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return ratingService.getRatingByID(id);
    }

    @GetMapping(path = "/all")
    public Object getAllRating() {
        return ratingService.getAllRatings();
    }
    
    @GetMapping(path = "/allByProductId/{productId}")
    public Object getAllByProductId(@PathVariable(name = "productId") Integer productId) {
        return ratingService.getAllRatingByProductId(productId);
    }
    
    @GetMapping(path = "/allByShopId/{shopId}")
    public Object getAllByShopId(@PathVariable(name = "shopId") Integer shopId) {
        return ratingService.getAllRatingByShopId(shopId);
    }

	@PostMapping(path = "/save") 
	public Object addRating(@RequestBody RatingDto rating) { 
		return ratingService.saveRating(rating); 
	}

    @PostMapping(path = "/delete/{id}")
    public Object deleteRatingByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return ratingService.deleteRatingByID(id);
    }
    
    @GetMapping(path = "/allByCreatedByAndProductId/{createdBy}/{productId}")
    public Object getAllByProductId(@PathVariable(name = "createdBy") Integer createdBy, @PathVariable(name = "productId") Integer productId) {
        return ratingService.getRatingByCreatedByAndProductId(createdBy, productId);
    }

}
