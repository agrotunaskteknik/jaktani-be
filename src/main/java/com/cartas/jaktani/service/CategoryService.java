package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.CategoryDto;

public interface CategoryService {
	Object getCategoryByIDWithDocument(Integer id);
	Object getCategoryByID(Integer id);
	Object getAllCategorysWithDocument();
	Object getAllCategorys();
    Object deleteCategoryByID(Integer id);
    Object addCategory(CategoryDto categoryDto);
    Object editCategory(CategoryDto categoryDto);
    Object getAllWithSubCategoryById(Integer id);
}
