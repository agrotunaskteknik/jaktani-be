package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.DocumentDto;
import com.cartas.jaktani.model.Document;
import com.cartas.jaktani.repository.DocumentRepository;
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
public class DocumentServiceImpl implements DocumentService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    Integer SHOP_LOGO_DOC_TYPE = 1;
    Integer PRODUCT_DOC_TYPE = 2;
    Integer USER_DOC_TYPE = 3;
    Integer BANNER_DOC_TYPE = 4;
    Integer CATEGORY_DOC_TYPE = 5;
    Integer SUB_CATEGORY_DOC_TYPE = 6;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    DocumentService documentService;
    
    @Autowired
    private DocumentRepository repository;

    @Override
    public Object findDocumentByID(DocumentDto documentDto) {
        Optional<Document> document = repository.findByIdAndStatusIsNot(documentDto.getId(),STATUS_DELETED);
        if(!document.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(document.get()), HttpStatus.OK);
    }

    @Override
    public Object deleteDocumentByID(DocumentDto documentDto) {
    	Optional<Document> document = repository.findById(documentDto.getId());
    	if(!document.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		document.get().setStatus(STATUS_DELETED);
        	repository.save(document.get());
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
    public Object addDocument(DocumentDto document) {
    	Document entity = new Document();
    	if(!validateRequest(document, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setRefferenceId(document.getRefferenceId());
    		entity.setName(document.getName());
    		entity.setType(document.getType());
    		entity.setCode(document.getCode());
    		entity.setFormat(document.getFormat());
    		entity.setContentData(document.getContentData());
    		entity.setSize(document.getSize());
    		entity.setOrderNumber(document.getOrderNumber());
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setStatus(STATUS_DEFAULT);
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
    public Object editDocument(DocumentDto document) {
    	Document entity = new Document();
    	if(!validateRequest(document, EDIT_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	Optional<Document> documentById  = repository.findByIdAndStatusIsNot(document.getId(), STATUS_DELETED);
    	if(!documentById.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Document is not exist");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity = documentById.get();
    		entity.setRefferenceId(document.getRefferenceId());
    		entity.setName(document.getName());
    		entity.setType(document.getType());
    		entity.setCode(document.getCode());
    		entity.setFormat(document.getFormat());
    		entity.setContentData(document.getContentData());
    		entity.setSize(document.getSize());
    		entity.setOrderNumber(document.getOrderNumber());
    		entity.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
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
    public Object findAllByType(DocumentDto document) {
        List<Document> documents= repository.findAllByTypeAndStatusIsNotOrderByOrderNumberAsc(document.getType(), STATUS_DELETED);
        List<Document> documentList = new ArrayList<>();
        if(documents!=null) {
        	documentList = documents;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(documentList), HttpStatus.OK);
    }
    
    @Override
    public Object findAllByRefferenceIdAndType(DocumentDto document) {
        List<Document> documents= repository.findAllByRefferenceIdAndTypeAndStatusIsNotOrderByOrderNumberAsc(document.getRefferenceId(), document.getType(), STATUS_DELETED);
        List<Document> documentList = new ArrayList<>();
        if(documents!=null) {
        	documentList = documents;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(documentList), HttpStatus.OK);
    }
    
    @Override
    public Object findAllByRefferenceIdAndTypeAndCode(DocumentDto document) {
        List<Document> documents= repository.findAllByRefferenceIdAndTypeAndCodeAndStatusIsNotOrderByOrderNumberAsc(document.getRefferenceId(), document.getType(), document.getCode(), STATUS_DELETED);
        List<Document> documentList = new ArrayList<>();
        if(documents!=null) {
        	documentList = documents;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(documentList), HttpStatus.OK);
    }
    
    private Boolean validateRequest(DocumentDto document, Integer type) {
    	if(document.getName()==null || document.getName()=="" || document.getType()==null) {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(document.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
