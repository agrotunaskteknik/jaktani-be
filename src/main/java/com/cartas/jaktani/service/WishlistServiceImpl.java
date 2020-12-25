package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.WishlistDto;
import com.cartas.jaktani.model.Wishlist;
import com.cartas.jaktani.repository.WishlistRepository;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;
import com.cartas.jaktani.util.Utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class WishlistServiceImpl implements WishlistService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_LIKE = 1;
    Integer STATUS_UNLIKE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    WishlistService subWishlistService;
    
    @Autowired
    private WishlistRepository repository;

    @Override
    public Object getWishlistByID(Integer id) {
        Optional<Wishlist> wishlist = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!wishlist.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(wishlist.get()), HttpStatus.OK);
    }


    @Override
    public Object getAllWishlist() {
        List<Wishlist> wishlists= repository.findAllWishlistByAndStatusIsNot(STATUS_DELETED);
        List<Wishlist> wishlistList = new ArrayList<>();
        if(wishlists!=null) {
        	wishlistList = wishlists;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(wishlistList), HttpStatus.OK);
    }
    
    @Override
    public Object saveWishlist(WishlistDto wishlist) {
    	Wishlist entity = new Wishlist();
    	if(!validateRequest(wishlist)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Wishlist> isExistWishlist = repository.findFirstByProductIdAndUserIdAndStatusIsNot(wishlist.getProductId(), wishlist.getUserId(), STATUS_DELETED);
    	if(isExistWishlist.isPresent()) {
    		entity = isExistWishlist.get();
    		entity.setStatus(wishlist.getStatus());
    		entity.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    	}else {
    		entity.setProductId(wishlist.getProductId());
    		entity.setUserId(wishlist.getUserId());
    		entity.setStatus(wishlist.getStatus());
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    	}
    	
    	try {
    		repository.save(entity);
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Save Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    
    
    @Override
    public Object getAllWishlistByUserId(Integer userId) {
        List<Wishlist> wishlists= repository.findAllWishlistByUserIdAndStatusIsNot(userId, STATUS_DELETED);
        List<Wishlist> wishlistList = new ArrayList<>();
        if(wishlists!=null) {
        	wishlistList = wishlists;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(wishlistList), HttpStatus.OK);
    }
    
    private Boolean validateRequest(WishlistDto wishlist) {
    	if(wishlist.getProductId()==null && wishlist.getUserId()==null) {
    		return false;
    	}
    	
    	return true;
    }

}
