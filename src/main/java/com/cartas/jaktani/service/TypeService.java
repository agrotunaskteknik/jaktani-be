package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.TypeDto;

public interface TypeService {
	Object getTypeByID(Integer id);
	Object getAllTypes();
    Object deleteTypeByID(Integer id);
    Object addType(TypeDto typeDto);
    Object editType(TypeDto typeDto);
    Object getAllTypesByTypeGroupId(Integer typeGroupId);
}
