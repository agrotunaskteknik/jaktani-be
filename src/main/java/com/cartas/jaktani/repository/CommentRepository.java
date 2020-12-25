package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer> {
	Optional<Comment> findByIdAndStatusIsNot(Integer id, Integer status);
	List<Comment> findAllCommentByAndStatusIsNot(Integer status);
	List<Comment> findAllByProductIdAndStatusIsNot(Integer productId, Integer status);
	List<Comment> findAllByShopIdAndStatusIsNot(Integer shopId, Integer status);
}
