package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Product;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByNameAndStatusIsNot(String name, Integer status);
    Optional<Product> findByIdAndStatusIsNot(Integer id, Integer status);
    Optional<Product> findByNameAndShopIdAndStatusIsNot(String name, Integer shopId, Integer status);
    Optional<Product> findByNameAndShopIdAndIdIsNotAndStatusIsNot(String name, Integer shopId, Integer id, Integer status);
    List<Product> findAllProductByAndStatusIsNot(Integer status);
    List<Product> findAllProductByShopIdAndStatusIsNot(Integer shopId, Integer status);
}
