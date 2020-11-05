package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
	List<Type> findAllTypeByAndStatusIsNot(Integer status);
	Optional<Type> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<Type> findFirstByNameAndStatusIsNot(String name, Integer status);
	Optional<Type> findFirstByNameAndIdIsNotAndStatusIsNot(String name, Integer id, Integer status);
}
