package com.cartas.jaktani.model;

import javax.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "type")
public class Type {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	
	@Column(name = "name")
	public String name;
	
    @Column(name = "status")
    public Integer status;
    
    @Column(name = "created_time")
    public Timestamp createdTime;
    
    @Column(name = "category_id")
    public Integer categoryId;

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

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
}
