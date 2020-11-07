package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
	ProductType findFirstByProductIdAndTypeId(Integer productId, Integer typeId);
    ProductType findFirstByProductIdAndTypeIdAndStatusIsNot(Integer productId, Integer typeId, Integer status);
    List<ProductType> findAllByProductIdAndStatusIsNot(Integer productId, Integer status);
}
