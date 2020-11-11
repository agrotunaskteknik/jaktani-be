package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.SubCategoryDto;
import com.cartas.jaktani.model.SubCategory;
import com.cartas.jaktani.repository.SubCategoryRepository;
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
public class SubCategoryServiceImpl implements SubCategoryService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private SubCategoryRepository repository;

    @Override
    public Object getSubCategoryByID(Integer id) {
        Optional<SubCategory> subCategory = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!subCategory.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(subCategory.get()), HttpStatus.OK);
    }


    @Override
    public Object getAllSubCategorys() {
        List<SubCategory> subCategorys= repository.findAllSubCategoryByAndStatusIsNot(STATUS_DELETED);
        List<SubCategory> subCategoryList = new ArrayList<>();
        if(subCategorys!=null) {
        	subCategoryList = subCategorys;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(subCategoryList), HttpStatus.OK);
    }

    @Override
    public Object deleteSubCategoryByID(Integer id) {
    	Optional<SubCategory> subCategory = repository.findById(id);
    	if(!subCategory.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		subCategory.get().setStatus(STATUS_DELETED);
        	repository.save(subCategory.get());
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
    public Object addSubCategory(SubCategoryDto subCategory) {
    	SubCategory entity = new SubCategory();
    	if(!validateRequest(subCategory, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<SubCategory> isExistSubCategory = repository.findFirstByNameAndCategoryIdAndStatusIsNot(subCategory.getName(), subCategory.getCategoryId(), STATUS_DELETED);
    	if(isExistSubCategory.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("SubCategory name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(subCategory.getName());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
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
    public Object editSubCategory(SubCategoryDto subCategory) {
    	SubCategory entity = new SubCategory();
    	if(!validateRequest(subCategory, EDIT_TYPE) && subCategory.getId()!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<SubCategory> subCategoryById  = repository.findByIdAndStatusIsNot(subCategory.getId(), STATUS_DELETED);
    	if(!subCategoryById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("SubCategory is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<SubCategory> isExistSubCategory = repository.findFirstByNameAndCategoryIdAndIdIsNotAndStatusIsNot(subCategory.getName(), subCategory.getCategoryId(), subCategory.getId(), STATUS_DELETED);
    	if(isExistSubCategory.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("SubCategory name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = subCategoryById.get();
    		entity.setName(subCategory.getName());
    		entity.setCategoryId(subCategory.getCategoryId());
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
    
    @Override
    public List<SubCategory> getAllByCategoryId(Integer categoryId) {
        List<SubCategory> subCategorys= repository.findAllSubCategoryByCategoryIdAndStatusIsNot(categoryId, STATUS_DELETED);
        List<SubCategory> subCategoryList = new ArrayList<>();
        if(subCategorys!=null) {
        	subCategoryList = subCategorys;
        }
        return subCategoryList;
    }
    
    private Boolean validateRequest(SubCategoryDto subCategory, Integer type) {
    	if(subCategory.getName()==null || subCategory.getName()=="") {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(subCategory.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
