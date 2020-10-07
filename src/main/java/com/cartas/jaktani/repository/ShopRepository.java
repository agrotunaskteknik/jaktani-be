package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Integer> {
    Optional<Shop> findByName(String name);
    Optional<Shop> findById(Integer id);
}
