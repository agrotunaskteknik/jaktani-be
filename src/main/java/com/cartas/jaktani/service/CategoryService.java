package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.CategoryDto;

public interface CategoryService {
	Object getCategoryByID(Integer id);
	Object getAllCategorys();
	Object getAllCategoryName();
    Object deleteCategoryByID(Integer id);
    Object addCategory(CategoryDto categoryDto);
    Object editCategory(CategoryDto categoryDto);
    Object getAllWithSubCategoryById(Integer id);
}
