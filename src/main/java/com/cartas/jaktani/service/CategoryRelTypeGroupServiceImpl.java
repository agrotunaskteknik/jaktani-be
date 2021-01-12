package com.cartas.jaktani.service;

import com.cartas.jaktani.model.Category;
import com.cartas.jaktani.model.CategoryRelTypeGroup;
import com.cartas.jaktani.model.TypeGroup;
import com.cartas.jaktani.repository.CategoryRelTypeGroupRepository;
import com.cartas.jaktani.repository.CategoryRepository;
import com.cartas.jaktani.repository.TypeGroupRepository;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryRelTypeGroupServiceImpl implements CategoryRelTypeGroupService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired private CategoryRelTypeGroupRepository repository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private TypeGroupRepository typeGroupRepository;
    
    @Override
    public Object getByID(Integer id) {
        CategoryRelTypeGroup findById = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(findById!=null) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        Optional<Category> category = categoryRepository.findByIdAndStatusIsNot(findById.getCategoryId(), STATUS_DELETED);
        Optional<TypeGroup> typeGroup = typeGroupRepository.findByIdAndStatusIsNot(findById.getTypeGroupId(), STATUS_DELETED);
        if(category.isPresent()) {
        	findById.setCategoryName(category.get().getName());
        }
        if(typeGroup.isPresent()) {
        	findById.setTypeGroupName(typeGroup.get().getName());
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(findById), HttpStatus.OK);
    }
    
    @Override
    public Object getAll() {
    	List<CategoryRelTypeGroup> categoryRelTypeGroups = repository.findAllByStatusIsNot(STATUS_DELETED);
    	List<CategoryRelTypeGroup> categoryRelTypeGroupList = new ArrayList<>();
        if(categoryRelTypeGroups!=null) {
        	for(CategoryRelTypeGroup categoryRelTypeGroup: categoryRelTypeGroups) {
        		 Optional<Category> category = categoryRepository.findByIdAndStatusIsNot(categoryRelTypeGroup.getCategoryId(), STATUS_DELETED);
        	        Optional<TypeGroup> typeGroup = typeGroupRepository.findByIdAndStatusIsNot(categoryRelTypeGroup.getTypeGroupId(), STATUS_DELETED);
        	        if(category.isPresent()) {
        	        	categoryRelTypeGroup.setCategoryName(category.get().getName());
        	        }
        	        if(typeGroup.isPresent()) {
        	        	categoryRelTypeGroup.setTypeGroupName(typeGroup.get().getName());
        	        }
        		categoryRelTypeGroupList.add(categoryRelTypeGroup);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryRelTypeGroupList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllByCategoryId(Integer categoryId) {
    	List<CategoryRelTypeGroup> categoryRelTypeGroups = repository.findAllByCategoryIdAndStatusIsNot(categoryId, STATUS_DELETED);
    	List<CategoryRelTypeGroup> categoryRelTypeGroupList = new ArrayList<>();
        if(categoryRelTypeGroups!=null) {
        	for(CategoryRelTypeGroup categoryRelTypeGroup: categoryRelTypeGroups) {
        		 Optional<Category> category = categoryRepository.findByIdAndStatusIsNot(categoryRelTypeGroup.getCategoryId(), STATUS_DELETED);
        	        Optional<TypeGroup> typeGroup = typeGroupRepository.findByIdAndStatusIsNot(categoryRelTypeGroup.getTypeGroupId(), STATUS_DELETED);
        	        if(category.isPresent()) {
        	        	categoryRelTypeGroup.setCategoryName(category.get().getName());
        	        }
        	        if(typeGroup.isPresent()) {
        	        	categoryRelTypeGroup.setTypeGroupName(typeGroup.get().getName());
        	        }
        		categoryRelTypeGroupList.add(categoryRelTypeGroup);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryRelTypeGroupList), HttpStatus.OK);
    }
    

    @Override
    public Object deleteByID(Integer id) {
    	CategoryRelTypeGroup findById = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(findById==null) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
    	
    	try {
    		findById.setStatus(STATUS_DELETED);
        	repository.save(findById);
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
    public Object add(CategoryRelTypeGroup categoryRelTypeGroup) {
    	CategoryRelTypeGroup entity = new CategoryRelTypeGroup();
    	if(!validateRequest(categoryRelTypeGroup, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	CategoryRelTypeGroup isAlreadyExist= repository.findFirstByCategoryIdAndTypeGroupIdAndStatusIsNot(categoryRelTypeGroup.getCategoryId(), categoryRelTypeGroup.getTypeGroupId(), STATUS_DELETED);
    	if(isAlreadyExist!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data already exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setCategoryId(categoryRelTypeGroup.getCategoryId());
    		entity.setTypeGroupId(categoryRelTypeGroup.getTypeGroupId());
    		entity.setStatus(STATUS_ACTIVE);
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
    public Object edit(CategoryRelTypeGroup categoryRelTypeGroup) {
    	CategoryRelTypeGroup entity = new CategoryRelTypeGroup();
    	if(!validateRequest(categoryRelTypeGroup, EDIT_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	CategoryRelTypeGroup isAlreadyExist= repository.findFirstByCategoryIdAndTypeGroupIdAndIdIsNotAndStatusIsNot(categoryRelTypeGroup.getCategoryId(), categoryRelTypeGroup.getTypeGroupId(), categoryRelTypeGroup.getId(), STATUS_DELETED);
    	if(isAlreadyExist!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data already exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setCategoryId(categoryRelTypeGroup.getCategoryId());
    		entity.setTypeGroupId(categoryRelTypeGroup.getTypeGroupId());
    		entity.setStatus(categoryRelTypeGroup.getStatus());
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
    
    private Boolean validateRequest(CategoryRelTypeGroup categoryRelTypeGroup, Integer type) {
    	if(categoryRelTypeGroup.getCategoryId()==null || categoryRelTypeGroup.getTypeGroupId()==null) {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(categoryRelTypeGroup.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
