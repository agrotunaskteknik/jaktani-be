package com.cartas.jaktani.dto;

import java.util.List;

public class CategoryDto {
    public Integer id;
	public String name;
    public Integer status;
    public List<SubCategoryDto> subCategoryDto;
    public DocumentDto documentDto;

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

	public List<SubCategoryDto> getSubCategoryDto() {
		return subCategoryDto;
	}

	public void setSubCategoryDto(List<SubCategoryDto> subCategoryDto) {
		this.subCategoryDto = subCategoryDto;
	}

	public DocumentDto getDocumentDto() {
		return documentDto;
	}

	public void setDocumentDto(DocumentDto documentDto) {
		this.documentDto = documentDto;
	}
	
	
}
