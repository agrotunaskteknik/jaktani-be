package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.TypeDto;

public interface TypeService {
	Object getTypeByID(Integer id);
	Object getAllTypes();
    Object deleteTypeByID(Integer id);
    Object addType(TypeDto categoryDto);
    Object editType(TypeDto categoryDto);
}
