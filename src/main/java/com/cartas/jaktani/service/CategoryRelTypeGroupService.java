package com.cartas.jaktani.service;

import com.cartas.jaktani.model.CategoryRelTypeGroup;

public interface CategoryRelTypeGroupService {
	Object getByID(Integer id);
	Object getAll();
    Object deleteByID(Integer id);
    Object add(CategoryRelTypeGroup categoryRelTypeGroup);
    Object edit(CategoryRelTypeGroup categoryRelTypeGroup);
    Object getAllByCategoryId(Integer categoryId);
}
