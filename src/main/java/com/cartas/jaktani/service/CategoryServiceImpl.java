package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.CategoryDto;
import com.cartas.jaktani.dto.SubCategoryDto;
import com.cartas.jaktani.model.Category;
import com.cartas.jaktani.model.SubCategory;
import com.cartas.jaktani.repository.CategoryRepository;
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
public class CategoryServiceImpl implements CategoryService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    SubCategoryService subCategoryService;
    
    @Autowired
    private CategoryRepository repository;

    @Override
    public Object getCategoryByID(Integer id) {
        Optional<Category> category = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!category.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(category.get()), HttpStatus.OK);
    }


    @Override
    public Object getAllCategorys() {
        List<Category> categorys= repository.findAllCategoryByAndStatusIsNot(STATUS_DELETED);
        List<Category> categoryList = new ArrayList<>();
        if(categorys!=null) {
        	categoryList = categorys;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryList), HttpStatus.OK);
    }

    @Override
    public Object deleteCategoryByID(Integer id) {
    	Optional<Category> category = repository.findById(id);
    	if(!category.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		category.get().setStatus(STATUS_DELETED);
        	repository.save(category.get());
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
    public Object addCategory(CategoryDto category) {
    	Category entity = new Category();
    	if(!validateRequest(category, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Category> isExistCategory = repository.findFirstByNameAndStatusIsNot(category.getName(), STATUS_DELETED);
    	if(isExistCategory.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Category name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(category.getName());
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
    public Object editCategory(CategoryDto category) {
    	Category entity = new Category();
    	if(!validateRequest(category, EDIT_TYPE) && category.getId()!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Category> categoryById  = repository.findByIdAndStatusIsNot(category.getId(), STATUS_DELETED);
    	if(!categoryById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Category is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Category> isExistCategory = repository.findFirstByNameAndIdIsNotAndStatusIsNot(category.getName(), category.getId(), STATUS_DELETED);
    	if(isExistCategory.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Category name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = categoryById.get();
    		entity.setName(category.getName());
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
    public Object getAllWithSubCategoryById(Integer id) {
        List<Category> categorys= repository.findAllCategoryByAndStatusIsNot(STATUS_DELETED);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        if(categorys!=null) {
        	for(Category category: categorys) {
        		CategoryDto categoryDto = new CategoryDto();
        		categoryDto.setName(category.getName());
        		categoryDto.setId(category.getId());
        		
        		List<SubCategoryDto> subCategoryDtoList = new ArrayList<>();
        		List<SubCategory> subCategorys = subCategoryService.getAllByCategoryId(category.getId());
        		for(SubCategory subCategory: subCategorys) {
        			SubCategoryDto subCategoryDto = new SubCategoryDto();
        			subCategoryDto.setName(subCategory.getName());
        			subCategoryDto.setCategoryId(subCategory.getCategoryId());
        			subCategoryDtoList.add(subCategoryDto);
        		}
        		categoryDto.setSubCategoryDto(subCategoryDtoList);
        		categoryDtoList.add(categoryDto);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryDtoList), HttpStatus.OK);
    }
    
    private Boolean validateRequest(CategoryDto category, Integer type) {
    	if(category.getName()==null && category.getName()=="") {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(category.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
