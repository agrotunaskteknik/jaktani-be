package com.cartas.jaktani.repository;

import com.cartas.jaktani.model.CategoryRelTypeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRelTypeGroupRepository extends JpaRepository<CategoryRelTypeGroup, Integer> {
	CategoryRelTypeGroup findByIdAndStatusIsNot(Integer id, Integer status);
	CategoryRelTypeGroup findFirstByCategoryIdAndTypeGroupIdAndIdIsNotAndStatusIsNot(Integer categoryId, Integer typeGroupId, Integer id, Integer status);
	CategoryRelTypeGroup findFirstByCategoryIdAndTypeGroupIdAndStatusIsNot(Integer categoryId, Integer typeGroupId, Integer status);
    List<CategoryRelTypeGroup> findAllByStatusIsNot(Integer status);
    List<CategoryRelTypeGroup> findAllByCategoryIdAndStatusIsNot(Integer categoryId, Integer status);
    List<CategoryRelTypeGroup> findAllByTypeGroupIdAndStatusIsNot(Integer typeGroupId, Integer status);
}
