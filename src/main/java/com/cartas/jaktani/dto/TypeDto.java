package com.cartas.jaktani.dto;

public class TypeDto {
    public Integer id;
	public String name;
    public Integer status;
    public Integer typeGroupId;

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

	public Integer getTypeGroupId() {
		return typeGroupId;
	}

	public void setTypeGroupId(Integer typeGroupId) {
		this.typeGroupId = typeGroupId;
	}
	
}
