package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.ParamRequestDto;
import com.cartas.jaktani.model.Document;
import com.cartas.jaktani.model.ProductType;
import com.cartas.jaktani.model.VwProductDetails;
import com.cartas.jaktani.repository.DocumentRepository;
import com.cartas.jaktani.repository.ProductTypeRepository;
import com.cartas.jaktani.repository.VwProductDetailsRepository;
import com.cartas.jaktani.util.BaseResponse;
import com.cartas.jaktani.util.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VwProductDetailsServiceImpl implements VwProductDetailsService {
    Integer PRODUCT_DOC_TYPE = 2;
    Integer STATUS_DELETED = 0;
    
    BaseResponse response = new BaseResponse();

    @Autowired private VwProductDetailsRepository repository;
    @Autowired private ProductTypeRepository productTypeRepo;
    @Autowired private DocumentRepository documentRepo;
    

    @Override
    public Object findByProductId(Integer productId) {
    	if(productId==null) {
       	 	response.setResponseCode("FAILED");
            response.setResponseMessage("Bad Request");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }

        VwProductDetails productDetails = null;
        try {
        	 productDetails = repository.findFirstByProductId(productId);
	         if(productDetails!=null) {
	        	 List<ProductType> productTypeList = productTypeRepo.findAllByProductIdAndStatusIsNot(productId, STATUS_DELETED);
	        	 List<Document> documentList = documentRepo.findAllByRefferenceIdAndTypeAndStatusIsNot(productId, PRODUCT_DOC_TYPE, STATUS_DELETED);
	             productDetails.setProductTypeList(productTypeList);
	             productDetails.setDocumentList(documentList);
	         }
             
		} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
        
        return new ResponseEntity<String>(JSONUtil.createJSON(productDetails), HttpStatus.OK);
    }
    
    @Override
    public Object findAllByShopId(Integer shopId) {
    	 if(shopId==null) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Bad Request");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
    	
    	List<VwProductDetails> productDetailsList = new ArrayList<>(); 
        List<VwProductDetails> findProductDetails = repository.findAllByShopId(shopId);
        if(findProductDetails!=null && findProductDetails.size()>0) {
        	for(VwProductDetails productDetails :findProductDetails) {
        		List<ProductType> productTypeList = productTypeRepo.findAllByProductIdAndStatusIsNot(productDetails.getProductId(), STATUS_DELETED);
           	    List<Document> documentList = documentRepo.findAllByRefferenceIdAndTypeAndStatusIsNot(productDetails.getProductId(), PRODUCT_DOC_TYPE, STATUS_DELETED);
                productDetails.setProductTypeList(productTypeList);
                productDetails.setDocumentList(documentList);
                productDetailsList.add(productDetails);
        	}
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(productDetailsList), HttpStatus.OK);
    }
    
    @Override
    public Object searchAllProduct(ParamRequestDto paramRequestDto) {
    	List<VwProductDetails> productDetailsList = new ArrayList<>();
    	String keySearch = null;
    	if(paramRequestDto.pageSize == null || paramRequestDto.pageNumber == null) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Bad Request");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
    	if(paramRequestDto.getKeySearch()!=null && paramRequestDto.getKeySearch()!="") {
    		keySearch = paramRequestDto.getKeySearch().toLowerCase();
    	}

     	Pageable pageable = PageRequest.of(paramRequestDto.getPageNumber(), paramRequestDto.getPageSize());
     	try {
     		List<VwProductDetails> findProductDetails = null;
     		if(keySearch!=null) {
     			findProductDetails = repository.findAllWithKeySearch(pageable, keySearch);
     		}else {
     			findProductDetails = repository.findAllWithPaging(pageable);
     		}
            
            if(findProductDetails!=null && findProductDetails.size()>0) {
            	for(VwProductDetails productDetails :findProductDetails) {
            		List<ProductType> productTypeList = productTypeRepo.findAllByProductIdAndStatusIsNot(productDetails.getProductId(), STATUS_DELETED);
               	    List<Document> documentList = documentRepo.findAllByRefferenceIdAndTypeAndStatusIsNot(productDetails.getProductId(), PRODUCT_DOC_TYPE, STATUS_DELETED);
                    productDetails.setProductTypeList(productTypeList);
                    productDetails.setDocumentList(documentList);
                    productDetailsList.add(productDetails);
            	}
            }
     	} catch (Exception e) {
			response.setResponseCode("ERROR");
            response.setResponseMessage("Error "+e.getMessage());
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
		}
        
        return new ResponseEntity<String>(JSONUtil.createJSON(productDetailsList), HttpStatus.OK);
    }

}
