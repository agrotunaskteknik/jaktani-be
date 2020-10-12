package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Wishlist;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
	List<Wishlist> findAllWishlistByAndStatusIsNot(Integer status);
	Optional<Wishlist> findByIdAndStatusIsNot(Integer id, Integer status);
	List<Wishlist> findAllWishlistByUserIdAndStatusIsNot(Integer userId, Integer status);
	Optional<Wishlist> findFirstByProductIdAndUserIdAndStatusIsNot(Integer productId, Integer userId, Integer status);
	
}
