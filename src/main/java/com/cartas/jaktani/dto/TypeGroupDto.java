package com.cartas.jaktani.dto;

import java.util.List;

public class TypeGroupDto {
    public Integer id;
	public String name;
    public Integer status;
    public List<TypeDto> typeList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<TypeDto> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<TypeDto> typeList) {
		this.typeList = typeList;
	}
	
	
}
