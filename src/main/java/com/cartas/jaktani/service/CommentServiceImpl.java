package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.CommentDto;
import com.cartas.jaktani.model.Comment;
import com.cartas.jaktani.repository.CommentRepository;
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
public class CommentServiceImpl implements CommentService {
	Integer STATUS_DEFAULT = 1;
    Integer STATUS_DELETED = 0;
    Integer STATUS_ACTIVE = 1;
    Integer ADD_TYPE = 1;
    Integer EDIT_TYPE = 2;
    
    BaseResponse response = new BaseResponse();
    
    @Autowired
    private CommentRepository repository;

    @Override
    public Object getCommentByID(Integer id) {
        Optional<Comment> comment = repository.findByIdAndStatusIsNot(id,STATUS_DELETED);
        if(!comment.isPresent()) {
        	 response.setResponseCode("FAILED");
             response.setResponseMessage("Data not found");
             return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<String>(JSONUtil.createJSON(comment.get()), HttpStatus.OK);
    }

    @Override
    public Object getAllComments() {
        List<Comment> comments= repository.findAllCommentByAndStatusIsNot(STATUS_DELETED);
        List<Comment> commentList = new ArrayList<>();
        if(comments!=null) {
        	commentList = comments;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(commentList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllCommentByProductId(Integer productId) {
        List<Comment> comments= repository.findAllByProductIdAndStatusIsNot(productId, STATUS_DELETED);
        List<Comment> commentList = new ArrayList<>();
        if(comments!=null) {
        	commentList = comments;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(commentList), HttpStatus.OK);
    }
    
    @Override
    public Object getAllCommentByShopId(Integer shopId) {
        List<Comment> comments= repository.findAllByShopIdAndStatusIsNot(shopId, STATUS_DELETED);
        List<Comment> commentList = new ArrayList<>();
        if(comments!=null) {
        	commentList = comments;
        }
        return new ResponseEntity<String>(JSONUtil.createJSON(commentList), HttpStatus.OK);
    }

    @Override
    public Object deleteCommentByID(Integer id) {
    	Optional<Comment> comment = repository.findByIdAndStatusIsNot(id, STATUS_DELETED);
    	if(!comment.isPresent()) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data not found");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		comment.get().setStatus(STATUS_DELETED);
        	repository.save(comment.get());
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
    public Object addComment(CommentDto comment) {
    	Comment entity = new Comment();
    	if(!validateRequest(comment, ADD_TYPE)) {
    		response.setResponseCode("FAILED");
            response.setResponseMessage("Data is not valid");
            return new ResponseEntity<String>(JSONUtil.createJSON(response), HttpStatus.BAD_REQUEST);
    	}
    	
    	try {
    		entity.setProductId(comment.getProductId());
    		entity.setCreatedBy(comment.getCreatedBy());
    		entity.setComment(comment.getComment());
    		entity.setStatus(STATUS_DEFAULT);
    		entity.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
    		entity.setShopId(comment.getShopId());
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
    
    private Boolean validateRequest(CommentDto comment, Integer type) {
    	if(comment.getProductId()==null || comment.getShopId()==null || comment.getComment()==null  || comment.getComment()=="") {
    		return false;
    	}
    		
    	if(type == EDIT_TYPE) {
    		if(comment.getId()==null) {
    			return false;
    		}
    	}
    	return true;
    }

}
