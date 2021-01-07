package com.cartas.jaktani.model;

import javax.persistence.*;

import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;


@Entity
@Table(name = "product")
public class Product {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	
	@Column(name = "shop_id")
	public Integer shopId;
	
	@Column(name = "name")
	public String name;
	
	@Column(name = "description")
    public String description;
	
    @Column(name = "status")
    public Integer status;
    
    @Column(name = "created_time")
    public Timestamp createdTime;
    
    @Column(name = "updated_time")
    public Timestamp updatedTime;
    
    @Column(name = "stock")
    private Integer stock;
    
    @Column(name = "sold")
    private Integer sold;
    
    @Column(name = "min_order")
    private Integer minOrder;
    
    @Column(name = "max_order")
    private Integer maxOrder;
    
    //jenis satuan (kg, m, dll)
    @Column(name = "unit_type")
    private Integer unitType;
    
    //nilai satuan
    @Column(name = "unit_value")
    private BigDecimal unitValue;
    
    @Column(name = "category_id")
    private Integer categoryId;
    
    @Column(name = "type_group_id")
	public Integer typeGroupId;
    
    @Column(name = "type_id")
	public Integer typeId;
    
    @Column(name = "sub_category_id")
    private Integer subCategoryId;
    
    @Column(name = "brand")
	public String brand;
    
    @Column(name = "price")
	public BigInteger price;
    
    @Column(name = "discount")
    private Integer discount;
    
    @Column(name = "size")
	public String size;
    
    @Column(name = "youtube_link")
	public String youtubeLink;
    
    @Column(name = "condition")
    private Integer condition;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	
	public Timestamp getUpdatedTime() {
		return updatedTime;
	}
	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public Integer getSold() {
		return sold;
	}
	public void setSold(Integer sold) {
		this.sold = sold;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public Integer getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(Integer subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public BigInteger getPrice() {
		return price;
	}
	public void setPrice(BigInteger price) {
		this.price = price;
	}
	
	public Integer getDiscount() {
		return discount;
	}
	public void setDiscount(Integer discount) {
		this.discount = discount;
	}
	
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	
	public String getYoutubeLink() {
		return youtubeLink;
	}
	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}
	public Integer getMinOrder() {
		return minOrder;
	}
	public void setMinOrder(Integer minOrder) {
		this.minOrder = minOrder;
	}
	public Integer getMaxOrder() {
		return maxOrder;
	}
	public void setMaxOrder(Integer maxOrder) {
		this.maxOrder = maxOrder;
	}
	public Integer getUnitType() {
		return unitType;
	}
	public void setUnitType(Integer unitType) {
		this.unitType = unitType;
	}
	public BigDecimal getUnitValue() {
		return unitValue;
	}
	public void setUnitValue(BigDecimal unitValue) {
		this.unitValue = unitValue;
	}
	public Integer getCondition() {
		return condition;
	}
	public void setCondition(Integer condition) {
		this.condition = condition;
	}
	public Integer getTypeGroupId() {
		return typeGroupId;
	}
	public void setTypeGroupId(Integer typeGroupId) {
		this.typeGroupId = typeGroupId;
	}
	public Integer getTypeId() {
		return typeId;
	}
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
    
}
