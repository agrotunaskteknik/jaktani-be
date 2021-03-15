package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.CartItem;
import com.cartas.jaktani.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
