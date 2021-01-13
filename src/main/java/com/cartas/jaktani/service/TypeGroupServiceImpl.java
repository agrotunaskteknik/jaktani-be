package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.TypeGroupDto;
import com.cartas.jaktani.dto.DocumentDto;
import com.cartas.jaktani.dto.TypeDto;
import com.cartas.jaktani.model.TypeGroup;
import com.cartas.jaktani.model.CategoryRelTypeGroup;
import com.cartas.jaktani.model.Document;
import com.cartas.jaktani.model.Type;
import com.cartas.jaktani.repository.TypeGroupRepository;
import com.cartas.jaktani.repository.CategoryRelTypeGroupRepository;
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
public class TypeGroupServiceImpl implements TypeGroupService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    Integer CATEGORY_DOC_TYPE = 5;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired private TypeGroupRepository repository;
    @Autowired private TypeRepository typeRepository;
    @Autowired private CategoryRelTypeGroupRepository categoryRelTypeGroupRepository;

    @Override
    public Object getTypeGroupByID(Integer id) {
        Optional<TypeGroup> findTypeGroup = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!findTypeGroup.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        TypeGroup typeGroup = findTypeGroup.get();
        TypeGroupDto typeGroupDto = new TypeGroupDto();
		typeGroupDto.setName(typeGroup.getName());
		typeGroupDto.setId(typeGroup.getId());
		
        List<TypeDto> typeList = new ArrayList<>();
		List<Type> allTypes = typeRepository.findAllByTypeGroupIdAndStatusIsNot(typeGroup.getId(), STATUS_DELETED);
		if(allTypes!=null) {
			for(Type type: allTypes) {
    			TypeDto typeDto = new TypeDto();
    			typeDto.setId(type.getId());
    			typeDto.setName(type.getName());
    			typeDto.setTypeGroupId(type.getTypeGroupId());
    			typeDto.setStatus(type.getStatus());
    			typeList.add(typeDto);
    		}
		}
		
		typeGroupDto.setTypeList(typeList);
        return new ResponseEntity<String>(JSONUtil.createJSON(typeGroupDto), HttpStatus.OK);
    }

    @Override
    public Object getAllTypeGroups() {
    	List<TypeGroup> typeGroups= repository.findAllTypeGroupByAndStatusIsNot(STATUS_DELETED);
        List<TypeGroupDto> typeGroupDtoList = new ArrayList<>();
        if(typeGroups!=null) {
        	for(TypeGroup typeGroup: typeGroups) {
        		TypeGroupDto typeGroupDto = new TypeGroupDto();
        		typeGroupDto.setName(typeGroup.getName());
        		typeGroupDto.setId(typeGroup.getId());
        		List<Type> allTypes = typeRepository.findAllByTypeGroupIdAndStatusIsNot(typeGroup.getId(), STATUS_DELETED);
        		if(allTypes!=null) {
        	        List<TypeDto> typeList = new ArrayList<>();
        			for(Type type: allTypes) {
            			TypeDto typeDto = new TypeDto();
            			typeDto.setId(type.getId());
            			typeDto.setName(type.getName());
            			typeDto.setTypeGroupId(type.getTypeGroupId());
            			typeDto.setStatus(type.getStatus());
            			typeList.add(typeDto);
            		}
        			typeGroupDto.setTypeList(typeList);
        		}
        		typeGroupDtoList.add(typeGroupDto);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(typeGroupDtoList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllTypeGroupName() {
    	List<TypeGroup> typeGroups= repository.findAllTypeGroupByAndStatusIsNot(STATUS_DELETED);
        List<TypeGroupDto> typeGroupDtoList = new ArrayList<>();
        if(typeGroups!=null) {
        	for(TypeGroup typeGroup: typeGroups) {
        		TypeGroupDto typeGroupDto = new TypeGroupDto();
        		typeGroupDto.setName(typeGroup.getName());
        		typeGroupDto.setId(typeGroup.getId());
        		typeGroupDtoList.add(typeGroupDto);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(typeGroupDtoList), HttpStatus.OK);
    }

    @Override
    public Object deleteTypeGroupByID(Integer id) {
    	Optional<TypeGroup> typeGroup = repository.findById(id);
    	if(!typeGroup.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		if(isAlreadyUsed(id)) {
    			response.setResponseCode("FAILED");
                response.setResponseMessage("Data Already Used!");
                return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    		}
    		
    		typeGroup.get().setStatus(STATUS_DELETED);
        	repository.save(typeGroup.get());
        	
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
    public Object addTypeGroup(TypeGroupDto typeGroup) {
    	TypeGroup entity = new TypeGroup();
    	if(!validateRequest(typeGroup, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<TypeGroup> isExistTypeGroup = repository.findFirstByNameAndStatusIsNot(typeGroup.getName(), STATUS_DELETED);
    	if(isExistTypeGroup.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("TypeGroup name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(typeGroup.getName());
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
    public Object editTypeGroup(TypeGroupDto typeGroup) {
    	TypeGroup entity = new TypeGroup();
    	if(!validateRequest(typeGroup, EDIT_TYPE) && typeGroup.getId()!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<TypeGroup> typeGroupById  = repository.findByIdAndStatusIsNot(typeGroup.getId(), STATUS_DELETED);
    	if(!typeGroupById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("TypeGroup is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<TypeGroup> isExistTypeGroup = repository.findFirstByNameAndIdIsNotAndStatusIsNot(typeGroup.getName(), typeGroup.getId(), STATUS_DELETED);
    	if(isExistTypeGroup.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("TypeGroup name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = typeGroupById.get();
    		entity.setName(typeGroup.getName());
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
    
    private Boolean validateRequest(TypeGroupDto typeGroup, Integer type) {
    	if(typeGroup.getName()==null || typeGroup.getName()=="") {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(typeGroup.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }
    
    private Boolean isAlreadyUsed(Integer typeGroupId) {
    	List<CategoryRelTypeGroup> categoryRelTypeGroups = categoryRelTypeGroupRepository.findAllByCategoryIdAndStatusIsNot(typeGroupId, STATUS_DELETED);
    	if(categoryRelTypeGroups!=null && categoryRelTypeGroups.size()>0) {
    		return true;
    	}
    	return false;
    }
}
