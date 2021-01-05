package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByStatusAndUserID(Integer status, Long userID);
    Optional<CartItem> findByIdAndStatusIsNotAndUserID(Long id, Integer status, Long userID);
    Optional<CartItem> findByProductIDAndStatusAndUserID(Long productID, Integer status, Long userID);
    Optional<CartItem> findByIdAndProductIDAndStatusAndUserID(Long cartID, Long productID, Integer status, Long userID);
    Optional<CartItem> findByIdAndStatusAndUserID(Long id, Integer status, Long userID);
}
