package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
    Optional<Shop> findFirstByNameAndStatusIsNot(String name, Integer status);
    Optional<Shop> findByIdAndStatusIsNot(Integer id, Integer status);
    Optional<Shop> findFirstByNameAndIdIsNotAndStatusIsNot(String name, Integer id, Integer status);
    List<Shop> findAllShopByAndStatusIsNot(Integer status);
}
