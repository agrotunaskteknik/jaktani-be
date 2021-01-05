package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.TypeGroupDto;

public interface TypeGroupService {
	Object getTypeGroupByID(Integer id);
	Object getAllTypeGroups();
	Object getAllTypeGroupName();
    Object deleteTypeGroupByID(Integer id);
    Object addTypeGroup(TypeGroupDto typeGroupDto);
    Object editTypeGroup(TypeGroupDto typeGroupDto);
}
