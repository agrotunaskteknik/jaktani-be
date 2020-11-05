package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
	Optional<Rating> findByIdAndStatusIsNot(Integer id, Integer status);
	List<Rating> findAllRatingByAndStatusIsNot(Integer status);
	List<Rating> findAllByProductIdAndStatusIsNot(Integer productId, Integer status);
	List<Rating> findAllByShopIdAndStatusIsNot(Integer shopId, Integer status);
	Optional<Rating> findFirstByCreatedByAndProductIdAndStatusIsNot(Integer createdBy, Integer productId, Integer status);
}
