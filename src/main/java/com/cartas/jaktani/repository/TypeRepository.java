package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TypeRepository extends JpaRepository<Type, Integer> {
	List<Type> findAllTypeByAndStatusIsNot(Integer status);
	List<Type> findAllByCategoryIdAndStatusIsNot(Integer categoryId, Integer status);
	Optional<Type> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<Type> findFirstByNameAndCategoryIdAndIdIsNotAndStatusIsNot(String name, Integer cetegoryId, Integer id, Integer status);
	Optional<Type> findFirstByNameAndCategoryIdAndStatusIsNot(String name, Integer cetegoryId, Integer status);
}
