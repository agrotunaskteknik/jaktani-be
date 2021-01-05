package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.repository.ShopRepository;
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
public class ShopServiceImpl implements ShopService {
	Integer STATUS_DEFAULT = 1;
    static Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private ShopRepository repository;

    @Override
    public Object getShopByID(Integer id) {
        Optional<Shop> shop = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!shop.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(shop.get()), HttpStatus.OK);
    }

    @Override
    public Object getShopByName(String name) {
        Optional<Shop> shop = repository.findFirstByNameAndStatusIsNot(name, STATUS_DELETED);
        if(!shop.isPresent()) {
       	    response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
       }
        
       return new ResponseEntity<String>(JSONUtil.createJSON(shop), HttpStatus.OK);
    }

    @Override
    public Object getAllShops() {
        List<Shop> shops= repository.findAllShopByAndStatusIsNot(STATUS_DELETED);
        List<Shop> shopList = new ArrayList<>();
        if(shops!=null) {
        	shopList = shops;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(shopList), HttpStatus.OK);
    }

    @Override
    public Object deleteShopByID(Integer id) {
    	Optional<Shop> shop = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
    	if(!shop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		shop.get().setStatus(STATUS_DELETED);
        	repository.save(shop.get());
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
    public Object addShop(ShopDto shop) {
    	Shop entity = new Shop();
    	if(!validateRequest(shop, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> isExistShop = repository.findFirstByNameAndStatusIsNot(shop.getName(), STATUS_DELETED);
    	if(isExistShop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(shop.getName());
    		entity.setDescription(shop.getName());
    		entity.setUserID(shop.getUserID());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setPriority(2);
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
    
    @Override
    public Object editShop(ShopDto shop) {
    	Shop entity = new Shop();
    	if(!validateRequest(shop, EDIT_TYPE) && shop.getId()!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> shopById  = repository.findByIdAndStatusIsNot(shop.getId(), STATUS_DELETED);
    	if(!shopById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> isExistShop = repository.findFirstByNameAndIdIsNotAndStatusIsNot(shop.getName(), shop.getId(), STATUS_DELETED);
    	if(isExistShop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = shopById.get();
    		entity.setName(shop.getName());
    		entity.setDescription(shop.getName());
    		entity.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setUpdatedBy(shop.getUpdatedBy());
    		repository.save(entity);
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	
    	response.setResponseCode("SUCCESS");
        response.setResponseMessage("Edit Success");
        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.OK);
    }
    
    private Boolean validateRequest(ShopDto shop, Integer type) {
    	if(shop.getName()==null || shop.getName()=="" 
    		|| shop.getDescription()==null  || shop.getDescription()=="" 
    		|| shop.getUserID()==null) {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(shop.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
