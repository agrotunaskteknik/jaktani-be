package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.TypeDto;
import com.cartas.jaktani.model.Type;
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
public class TypeServiceImpl implements TypeService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private TypeRepository repository;

    @Override
    public Object getTypeByID(Integer id) {
        Optional<Type> type = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!type.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(type.get()), HttpStatus.OK);
    }


    @Override
    public Object getAllTypes() {
        List<Type> types= repository.findAllTypeByAndStatusIsNot(STATUS_DELETED);
        List<Type> typeList = new ArrayList<>();
        if(types!=null) {
        	typeList = types;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(typeList), HttpStatus.OK);
    }

    @Override
    public Object deleteTypeByID(Integer id) {
    	Optional<Type> type = repository.findById(id);
    	if(!type.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		type.get().setStatus(STATUS_DELETED);
        	repository.save(type.get());
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
    public Object addType(TypeDto type) {
    	Type entity = new Type();
    	if(!validateRequest(type, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Type> isExistType = repository.findFirstByNameAndCategoryIdAndStatusIsNot(type.getName(), type.getCategoryId(), STATUS_DELETED);
    	if(isExistType.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Type name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(type.getName());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCategoryId(type.getCategoryId());
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
    public Object editType(TypeDto type) {
    	Type entity = new Type();
    	if(!validateRequest(type, EDIT_TYPE) && type.getId()!=null) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Type> typeById  = repository.findByIdAndStatusIsNot(type.getId(), STATUS_DELETED);
    	if(!typeById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Type is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Type> isExistType = repository.findFirstByNameAndCategoryIdAndIdIsNotAndStatusIsNot(type.getName(), type.getCategoryId(), type.getId(), STATUS_DELETED);
    	if(isExistType.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Type name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = typeById.get();
    		entity.setName(type.getName());
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
    
    private Boolean validateRequest(TypeDto type, Integer action) {
    	if(type.getName()==null && type.getCategoryId()!=null && type.getName()=="") {
    		return false;
    	}
    		
    	if(action == EDIT_TYPE) {
    		if(type.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
