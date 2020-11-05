package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Integer> {
	List<SubCategory> findAllSubCategoryByAndStatusIsNot(Integer status);
	Optional<SubCategory> findByIdAndStatusIsNot(Integer id, Integer status);
	Optional<SubCategory> findFirstByNameAndCategoryIdAndStatusIsNot(String name, Integer categoryId, Integer status);
	Optional<SubCategory> findFirstByNameAndCategoryIdAndIdIsNotAndStatusIsNot(String name, Integer categoryId, Integer id, Integer status);
	List<SubCategory> findAllSubCategoryByCategoryIdAndStatusIsNot(Integer categoryId, Integer status);
}
