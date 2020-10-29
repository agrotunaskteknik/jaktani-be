package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ProductDto;
import com.cartas.jaktani.model.Product;
import com.cartas.jaktani.repository.ProductRepository;
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
public class ProductServiceImpl implements ProductService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private ProductRepository repository;

    @Override
    public Object getProductByID(Integer id) {
        Optional<Product> product = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!product.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(product.get()), HttpStatus.OK);
    }

    @Override
    public Object getProductByName(String name) {
    	List<Product> products = repository.findByNameAndStatusIsNot(name, STATUS_DELETED);
        List<Product> productList = new ArrayList<>();
        if(products!=null) {
        	productList = products;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productList), HttpStatus.OK);
    }

    @Override
    public Object getAllProducts() {
        List<Product> products= repository.findAllProductByAndStatusIsNot(STATUS_DELETED);
        List<Product> productList = new ArrayList<>();
        if(products!=null) {
        	productList = products;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productList), HttpStatus.OK);
    }

    @Override
    public Object deleteProductByID(Integer id) {
    	Optional<Product> product = repository.findById(id);
    	if(!product.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		product.get().setStatus(STATUS_DELETED);
        	repository.save(product.get());
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
    public Object addProduct(ProductDto product) {
    	Product entity = new Product();
    	if(!validateRequest(product, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Product> isExistProduct = repository.findByNameAndShopIdAndStatusIsNot(product.getName(), product.getShopId(), STATUS_DELETED);
    	if(isExistProduct.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Product name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setShopId(product.getShopId());
    		entity.setName(product.getName());
    		entity.setDescription(product.getName());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setStock(product.getStock());
    		entity.setMinOrder(product.getMinOrder());
    		entity.setMaxOrder(product.getMaxOrder());
    		entity.setUnitType(product.getUnitType());
    		entity.setUnitValue(product.getUnitValue());
    		entity.setTypeId(product.getTypeId());
    		entity.setSold(product.getSold());
    		entity.setCategoryId(product.getCategoryId());
    		entity.setSubCategoryId(product.getSubCategoryId());
    		entity.setBrand(product.getBrand());
    		entity.setPrice(product.getPrice());
    		entity.setDiscount(product.getDiscount());
    		entity.setSize(product.getSize());
    		entity.setYoutubeLink(product.getYoutubeLink());
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
    public Object editProduct(ProductDto product) {
    	Product entity = new Product();
    	if(!validateRequest(product, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Product> productById  = repository.findByIdAndStatusIsNot(product.getId(), STATUS_DELETED);
    	if(!productById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Shop is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Product> isExistProduct = repository.findByNameAndShopIdAndIdIsNotAndStatusIsNot(product.getName(),product.getShopId(), product.getId(), STATUS_DELETED);
    	if(isExistProduct.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Product name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = productById.get();
    		entity.setName(product.getName());
    		entity.setDescription(product.getName());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setStock(product.getStock());
    		entity.setSold(product.getSold());
    		entity.setMinOrder(product.getMinOrder());
    		entity.setMaxOrder(product.getMaxOrder());
    		entity.setUnitType(product.getUnitType());
    		entity.setUnitValue(product.getUnitValue());
    		entity.setTypeId(product.getTypeId());
    		entity.setCategoryId(product.getCategoryId());
    		entity.setSubCategoryId(product.getSubCategoryId());
    		entity.setBrand(product.getBrand());
    		entity.setPrice(product.getPrice());
    		entity.setDiscount(product.getDiscount());
    		entity.setSize(product.getSize());
    		entity.setYoutubeLink(product.getYoutubeLink());
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
    public Object getAllProductByShopId(Integer shopId) {
        List<Product> products= repository.findAllProductByShopIdAndStatusIsNot(shopId, STATUS_DELETED);
        List<Product> productList = new ArrayList<>();
        if(products!=null) {
        	productList = products;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productList), HttpStatus.OK);
    }
    
    private Boolean validateRequest(ProductDto product, Integer type) {
    	if(product.getName()==null && product.getName()=="" 
    	    && product.getDescription()==null  && product.getDescription()=="" 
    		&& product.getShopId()==null) {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(product.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
