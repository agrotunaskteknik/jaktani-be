package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.TypeGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeGroupRepository extends JpaRepository<TypeGroup, Integer> {
	List<TypeGroup> findAllTypeGroupByAndStatusIsNot(Integer status);
	Optional<TypeGroup> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<TypeGroup> findFirstByNameAndStatusIsNot(String name, Integer status);
	Optional<TypeGroup> findFirstByNameAndIdIsNotAndStatusIsNot(String name, Integer id, Integer status);
}
