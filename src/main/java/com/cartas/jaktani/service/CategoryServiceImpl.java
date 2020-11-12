package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.CategoryDto;
import com.cartas.jaktani.dto.DocumentDto;
import com.cartas.jaktani.dto.SubCategoryDto;
import com.cartas.jaktani.dto.TypeDto;
import com.cartas.jaktani.model.Category;
import com.cartas.jaktani.model.Document;
import com.cartas.jaktani.model.SubCategory;
import com.cartas.jaktani.model.Type;
import com.cartas.jaktani.repository.CategoryRepository;
import com.cartas.jaktani.repository.DocumentRepository;
import com.cartas.jaktani.repository.TypeRepository;
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
    
    Integer CATEGORY_DOC_TYPE = 5;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    SubCategoryService subCategoryService;
    
    @Autowired private CategoryRepository repository;
    @Autowired private DocumentRepository documentRepository;
    @Autowired private TypeRepository typeRepository;

    @Override
    public Object getCategoryByIDWithDocument(Integer id) {
        Optional<Category> findCategory = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!findCategory.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        Category category = new Category();
        CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName(category.getName());
		categoryDto.setId(category.getId());
		Optional<Document> findDocument = documentRepository.findByRefferenceIdAndTypeAndStatusIsNot(category.getId(), CATEGORY_DOC_TYPE, STATUS_DELETED);
		if(findDocument.isPresent()) {
			Document document = findDocument.get();
			DocumentDto documentDto = new DocumentDto();
			documentDto.setId(category.getId());
			
			documentDto.setRefferenceId(document.getRefferenceId());
			documentDto.setName(document.getName());
			documentDto.setType(document.getType());
			documentDto.setCode(document.getCode());
			documentDto.setFormat(document.getFormat());
			documentDto.setContentData(document.getContentData());
			documentDto.setSize(document.getSize());
			documentDto.setOrderNumber(document.getOrderNumber());
			documentDto.setStatus(document.getStatus());
			categoryDto.setDocumentDto(documentDto);

		}
        
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryDto), HttpStatus.OK);
    }
    
    @Override
    public Object getCategoryByID(Integer id) {
        Optional<Category> findCategory = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!findCategory.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        Category category = findCategory.get();
        CategoryDto categoryDto = new CategoryDto();
		categoryDto.setName(category.getName());
		categoryDto.setId(category.getId());
		
        List<TypeDto> typeList = new ArrayList<>();
		List<Type> allTypes = typeRepository.findAllByCategoryIdAndStatusIsNot(category.getId(), STATUS_DELETED);
		if(allTypes!=null) {
			for(Type type: allTypes) {
    			TypeDto typeDto = new TypeDto();
    			typeDto.setId(type.getId());
    			typeDto.setName(type.getName());
    			typeDto.setCategoryId(type.getCategoryId());
    			typeDto.setStatus(type.getStatus());
    			typeList.add(typeDto);
    		}
		}
		
		categoryDto.setTypeList(typeList);
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryDto), HttpStatus.OK);
    }


    @Override
    public Object getAllCategorysWithDocument() {
    	List<Category> categorys= repository.findAllCategoryByAndStatusIsNot(STATUS_DELETED);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        if(categorys!=null) {
        	for(Category category: categorys) {
        		CategoryDto categoryDto = new CategoryDto();
        		categoryDto.setName(category.getName());
        		categoryDto.setId(category.getId());
        		Optional<Document> findDocument = documentRepository.findByRefferenceIdAndTypeAndStatusIsNot(category.getId(), CATEGORY_DOC_TYPE, STATUS_DELETED);
        		if(findDocument.isPresent()) {
        			Document document = findDocument.get();
        			DocumentDto documentDto = new DocumentDto();
        			documentDto.setId(category.getId());
        			
        			documentDto.setRefferenceId(document.getRefferenceId());
        			documentDto.setName(document.getName());
        			documentDto.setType(document.getType());
        			documentDto.setCode(document.getCode());
        			documentDto.setFormat(document.getFormat());
        			documentDto.setContentData(document.getContentData());
        			documentDto.setSize(document.getSize());
        			documentDto.setOrderNumber(document.getOrderNumber());
        			documentDto.setStatus(document.getStatus());
        			categoryDto.setDocumentDto(documentDto);

        		}
        		categoryDtoList.add(categoryDto);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryDtoList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllCategorys() {
    	List<Category> categorys= repository.findAllCategoryByAndStatusIsNot(STATUS_DELETED);
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        List<TypeDto> typeList = new ArrayList<>();
        if(categorys!=null) {
        	for(Category category: categorys) {
        		CategoryDto categoryDto = new CategoryDto();
        		categoryDto.setName(category.getName());
        		categoryDto.setId(category.getId());
        		List<Type> allTypes = typeRepository.findAllByCategoryIdAndStatusIsNot(category.getId(), STATUS_DELETED);
        		if(allTypes!=null) {
        			for(Type type: allTypes) {
            			TypeDto typeDto = new TypeDto();
            			typeDto.setId(type.getId());
            			typeDto.setName(type.getName());
            			typeDto.setCategoryId(type.getCategoryId());
            			typeDto.setStatus(type.getStatus());
            			typeList.add(typeDto);
            		}
        		}
        		
        		categoryDto.setTypeList(typeList);
        		categoryDtoList.add(categoryDto);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(categoryDtoList), HttpStatus.OK);
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
        	
        	Optional<Document> findDocument = documentRepository.findByRefferenceIdAndTypeAndStatusIsNot(id, CATEGORY_DOC_TYPE, STATUS_DELETED);
    		if(findDocument.isPresent()) {
    			Document document = findDocument.get();
    			document.setStatus(STATUS_DELETED);
    			documentRepository.save(document);
    		}
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
    		
    		if(category.getDocumentDto()!=null) {
        		saveCategoryDocument(category.getDocumentDto(), entity.getId(), ADD_TYPE);
        	}
    		
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
    		
    		if(category.getDocumentDto()!=null) {
        		saveCategoryDocument(category.getDocumentDto(), entity.getId(), EDIT_TYPE);
        	}
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
        		Optional<Document> findDocument = documentRepository.findByRefferenceIdAndTypeAndStatusIsNot(category.getId(), CATEGORY_DOC_TYPE, STATUS_DELETED);
        		if(findDocument.isPresent()) {
        			Document document = findDocument.get();
        			DocumentDto documentDto = new DocumentDto();
        			documentDto.setId(id);
        			
        			documentDto.setRefferenceId(document.getRefferenceId());
        			documentDto.setName(document.getName());
        			documentDto.setType(document.getType());
        			documentDto.setCode(document.getCode());
        			documentDto.setFormat(document.getFormat());
        			documentDto.setContentData(document.getContentData());
        			documentDto.setSize(document.getSize());
        			documentDto.setOrderNumber(document.getOrderNumber());
        			documentDto.setStatus(document.getStatus());
        			categoryDto.setDocumentDto(documentDto);

        		}
        		
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
    	if(category.getName()==null || category.getName()=="") {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(category.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }
    
    private void saveCategoryDocument(DocumentDto document, Integer categoryId, Integer type) {
    	Document entity = new Document();
    	entity.setRefferenceId(categoryId);
		entity.setName(document.getName());
		entity.setType(CATEGORY_DOC_TYPE);
		entity.setCode(document.getCode());
		entity.setFormat(document.getFormat());
		entity.setContentData(document.getContentData());
		entity.setSize(document.getSize());
		entity.setOrderNumber(document.getOrderNumber());
		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
		entity.setStatus(STATUS_DEFAULT);
		
    	if(type == EDIT_TYPE) {
    		Optional<Document> findDocument = documentRepository.findByRefferenceIdAndTypeAndStatusIsNot(categoryId, CATEGORY_DOC_TYPE, STATUS_DELETED);
    		if(findDocument.isPresent()) {
    			entity.setId(findDocument.get().getId());
    		}
    	}
    	documentRepository.save(entity);
    }

}
