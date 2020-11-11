package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.VwProductDetails;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VwProductDetailsRepository extends JpaRepository<VwProductDetails, Integer> {
    List<VwProductDetails> findAllByShopId(Integer shopId);
    VwProductDetails findFirstByProductId(Integer productId);
}
