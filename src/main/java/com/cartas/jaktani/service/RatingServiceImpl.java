package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.RatingDto;
import com.cartas.jaktani.model.Rating;
import com.cartas.jaktani.repository.RatingRepository;
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
public class RatingServiceImpl implements RatingService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private RatingRepository repository;

    @Override
    public Object getRatingByID(Integer id) {
        Optional<Rating> rating = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!rating.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(rating.get()), HttpStatus.OK);
    }

    @Override
    public Object getAllRatings() {
        List<Rating> ratings= repository.findAllRatingByAndStatusIsNot(STATUS_DELETED);
        List<Rating> ratingList = new ArrayList<>();
        if(ratings!=null) {
        	ratingList = ratings;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(ratingList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllRatingByProductId(Integer productId) {
        List<Rating> ratings= repository.findAllByProductIdAndStatusIsNot(productId, STATUS_DELETED);
        List<Rating> ratingList = new ArrayList<>();
        if(ratings!=null) {
        	ratingList = ratings;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(ratingList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllRatingByShopId(Integer shopId) {
        List<Rating> ratings= repository.findAllByShopIdAndStatusIsNot(shopId, STATUS_DELETED);
        List<Rating> ratingList = new ArrayList<>();
        if(ratings!=null) {
        	ratingList = ratings;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(ratingList), HttpStatus.OK);
    }
    
    @Override
    public Object getRatingByCreatedByAndProductId(Integer createdBy, Integer productId) {
        Optional<Rating> rating = repository.findFirstByCreatedByAndProductIdAndStatusIsNot(createdBy, productId, STATUS_DELETED);
        if(!rating.isPresent()) {
        	response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(rating.get()), HttpStatus.OK);
    }

    @Override
    public Object deleteRatingByID(Integer id) {
    	Optional<Rating> rating = repository.findByIdAndStatusIsNot(id, STATUS_DELETED);
    	if(!rating.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		rating.get().setStatus(STATUS_DELETED);
        	repository.save(rating.get());
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Delete Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    @Override
    public Object saveRating(RatingDto rating) {
    	Rating entity = new Rating();
    	if(!validateRequest(rating, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Rating> currentRating = repository.findFirstByCreatedByAndProductIdAndStatusIsNot(rating.getCreatedBy(), rating.getProductId(), STATUS_DELETED);
    	if(currentRating.isPresent()) {//if exist than update
    		entity = currentRating.get();
    	}
    	
    	try {
    		entity.setProductId(rating.getProductId());
    		entity.setCreatedBy(rating.getCreatedBy());
    		entity.setScore(rating.getScore());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setDescription(rating.getDescription());
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setShopId(rating.getShopId());
    		repository.save(entity);
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Add Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    private Boolean validateRequest(RatingDto rating, Integer type) {
    	if(rating.getProductId()==null || rating.getCreatedBy()==null
    		||rating.getScore()==null || rating.getShopId()==null) {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(rating.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
