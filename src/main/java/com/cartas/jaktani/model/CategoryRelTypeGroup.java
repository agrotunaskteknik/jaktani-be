package com.cartas.jaktani.model;

import javax.persistence.*;


@Entity
@Table(name = "category_rel_type_group")
public class CategoryRelTypeGroup {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	@Column(name = "category_id")
    private Integer categoryId;
	@Column(name = "type_group_id")
    private Integer typeGroupId;
	@Column(name = "status")
    private Integer status;
	
	@Transient
    private String categoryName;
	
	@Transient
    private String typeGroupName;

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;										
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public Integer getTypeGroupId() {
		return typeGroupId;
	}

	public void setTypeGroupId(Integer typeGroupId) {
		this.typeGroupId = typeGroupId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getTypeGroupName() {
		return typeGroupName;
	}

	public void setTypeGroupName(String typeGroupName) {
		this.typeGroupName = typeGroupName;
	}
	
}
