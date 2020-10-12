package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
	List<Category> findAllCategoryByAndStatusIsNot(Integer status);
	Optional<Category> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<Category> findFirstByNameAndStatusIsNot(String name, Integer status);
	Optional<Category> findFirstByNameAndIdIsNotAndStatusIsNot(String name, Integer id, Integer status);
}
