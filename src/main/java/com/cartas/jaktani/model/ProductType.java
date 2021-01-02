package com.cartas.jaktani.model;

import javax.persistence.*;


@Entity
@Table(name = "product_type")
public class ProductType {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	@Column(name = "product_id")
    private Integer productId;
	@Column(name = "type_id")
    private Integer typeId;
	@Column(name = "status")
    private Integer status;
	
	@Transient
    private Integer typeGroupId;
	
	@Transient
    private String name;

	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;										
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
