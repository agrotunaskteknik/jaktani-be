package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
    List<ProductType> findAllByProductIdAndStatusIsNot(Integer productId, Integer status);
}
