package com.cartas.jaktani.service;

import java.util.List;

import com.cartas.jaktani.dto.SubCategoryDto;
import com.cartas.jaktani.model.SubCategory;

public interface SubCategoryService {
	Object getSubCategoryByID(Integer id);
	Object getAllSubCategorys();
    Object deleteSubCategoryByID(Integer id);
    Object addSubCategory(SubCategoryDto product);
    Object editSubCategory(SubCategoryDto product);
    List<SubCategory> getAllByCategoryId(Integer categoryId);
}
