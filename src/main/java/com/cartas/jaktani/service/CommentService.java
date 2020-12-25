package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.CommentDto;

public interface CommentService {
	Object getCommentByID(Integer userID);
	Object getAllComments();
    Object deleteCommentByID(Integer id);
    Object addComment(CommentDto comment);
    Object getAllCommentByProductId(Integer productId);
    Object getAllCommentByShopId(Integer shopId);
}
