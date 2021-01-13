package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.TypeDto;
import com.cartas.jaktani.model.Type;
import com.cartas.jaktani.model.TypeGroup;
import com.cartas.jaktani.repository.TypeGroupRepository;
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
    
    @Autowired private TypeRepository repository;
    @Autowired private TypeGroupRepository typeGroupRepository;

    @Override
    public Object getTypeByID(Integer id) {
        Optional<Type> typeById = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!typeById.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        Optional<TypeGroup> typeGroup = typeGroupRepository.findByIdAndStatusIsNot(typeById.get().getTypeGroupId(), STATUS_DELETED);
        TypeDto type = new TypeDto();
        type.setId(id);
        type.setName(typeById.get().getName());
        type.setStatus(typeById.get().getStatus());
        type.setTypeGroupId(typeById.get().getTypeGroupId());
        if(typeGroup.isPresent()) {
            type.setTypeGroupName(typeGroup.get().getName());
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(type), HttpStatus.OK);
    }


    @Override
    public Object getAllTypes() {
        List<Type> types = repository.findAllTypeByAndStatusIsNot(STATUS_DELETED);
        List<TypeDto> typeList = new ArrayList<>();
        if(types!=null) {
        	for(Type tempType: types) {
        		Optional<TypeGroup> typeGroup = typeGroupRepository.findByIdAndStatusIsNot(tempType.getTypeGroupId(), STATUS_DELETED);
                TypeDto type = new TypeDto();
                type.setId(tempType.getId());
                type.setName(tempType.getName());
                type.setStatus(tempType.getStatus());
                type.setTypeGroupId(tempType.getTypeGroupId());
                if(typeGroup.isPresent()) {
                    type.setTypeGroupName(typeGroup.get().getName());
                }
                typeList.add(type);
        	}
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
    	
    	Optional<Type> isExistType = repository.findFirstByNameAndTypeGroupIdAndStatusIsNot(type.getName(), type.getTypeGroupId(), STATUS_DELETED);
    	if(isExistType.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Type name alrady exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setName(type.getName());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setTypeGroupId(type.getTypeGroupId());
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
    	
    	Optional<Type> isExistType = repository.findFirstByNameAndTypeGroupIdAndIdIsNotAndStatusIsNot(type.getName(), type.getTypeGroupId(), type.getId(), STATUS_DELETED);
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
    	if(type.getName()==null || type.getTypeGroupId()==null || type.getName()=="") {
    		return false;
    	}
    		
    	if(action == EDIT_TYPE) {
    		if(type.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }
    
    @Override
    public Object getAllTypesByTypeGroupId(Integer typeGroupId) {
	   if(typeGroupId==null) {
			response.setResponseCode("FAILED");
	        response.setResponseMessage("typeGroupId is null");
	        return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
	   }
       List<TypeDto> typeList = new ArrayList<TypeDto>();
       List<Type> allTypes = repository.findAllByTypeGroupIdAndStatusIsNot(typeGroupId,STATUS_DELETED);
       if(allTypes!=null) {
       	for(Type tempType: allTypes) {
       		Optional<TypeGroup> typeGroup = typeGroupRepository.findByIdAndStatusIsNot(tempType.getTypeGroupId(), STATUS_DELETED);
               TypeDto type = new TypeDto();
               type.setId(tempType.getId());
               type.setName(tempType.getName());
               type.setStatus(tempType.getStatus());
               type.setTypeGroupId(tempType.getTypeGroupId());
               if(typeGroup.isPresent()) {
                   type.setTypeGroupName(typeGroup.get().getName());
               }
               typeList.add(type);
       	}
       }
       return new ResponseEntity<String>(JSONUtil.createJSON(typeList), HttpStatus.OK);
    }

}
