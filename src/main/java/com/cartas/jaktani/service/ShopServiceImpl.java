package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.model.Users;
import com.cartas.jaktani.repository.ShopRepository;
import com.cartas.jaktani.repository.UserRepository;
import com.cartas.jaktani.repository.wrapper.UserWrapper;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;
import com.cartas.jaktani.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ShopServiceImpl implements ShopService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private ShopRepository repository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Object getShopByID(Integer id) {
        Optional<Shop> shop = repository.findById(id);
        if(!shop.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(shop), HttpStatus.OK);
    }

    @Override
    public Object getShopByName(String name) {
        Optional<Shop> shop = repository.findByName(name);
        if(!shop.isPresent()) {
       	    response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
       }
        
       return new ResponseEntity<String>(JSONUtil.createJSON(shop), HttpStatus.OK);
    }

    @Override
    public Object getAllShops() {
        List<Shop> shops= repository.findAll();
        List<Shop> shopList = new ArrayList<>();
        if(shops!=null) {
        	shopList = shops;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(shopList), HttpStatus.OK);
    }

    @Override
    public Object deleteShopByID(Integer id) {
    	Optional<Shop> shop = repository.findById(id);
    	if(!shop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	shop.get().setStatus(STATUS_DELETED);
    	repository.save(shop.get());
    	return new ResponseEntity<String>(JSONUtil.createJSON(shop.get()), HttpStatus.OK);
    }
    
    @Override
    public Object addShop(ShopDto shop) {
    	Shop entity = new Shop();
    	if(!validateRequest(shop, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Shop> isExistShop = repository.findByName(shop.getName());
    	if(isExistShop.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shope name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(shop.getName());
    		entity.setDescription(shop.getName());
    		entity.setUserID(shop.getId());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setPriority(2);
    		repository.save(entity);
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
    	
    	return new ResponseEntity<String>(JSONUtil.createJSON(entity), HttpStatus.OK);
    }
    
    private Boolean validateRequest(ShopDto shop, Integer type) {
    	if(shop.getName()==null && shop.getName()=="" 
    	    && shop.getDescription()==null  && shop.getDescription()=="" 
    		&& shop.getUserID()==null) {
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
