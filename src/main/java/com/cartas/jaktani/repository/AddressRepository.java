package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Address;
import com.cartas.jaktani.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    Optional<Address> findByIdAndStatusIsNot(Integer id, Integer status);
    Optional<Address> findByStatus(Integer status);
    Optional<Address> findByTypeAndRelationIdAndStatusIs(Integer type, Integer relationId, Integer status);
    List<Address> findAllByTypeAndRelationIdAndStatusIsNot(Integer type, Integer relationId, Integer status);
}
