package com.cartas.jaktani.model;

import javax.persistence.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;


@Entity
@Table(name = "vw_product_details")
public class VwProductDetails {
	@Id
	@Column(name = "product_id")
    public Integer productId;
	
	@Column(name = "product_name")
	public String productName;
	
	@Column(name = "brand")
	public String brand;
    
    @Column(name = "price")
	public BigInteger price;
    
    @Column(name = "discount")
    private String discount;
    
    @Column(name = "current_price")
   	public Double currentPrice;
    
    @Column(name = "product_ratting_count")
   	public BigInteger productRattingCount;
    
    @Column(name = "product_ratting_average")
   	public Double productRattingAverage;

    @Column(name = "size")
   	public BigDecimal size;
    
    @Column(name = "sold")
    private Integer sold;
	
    @Column(name = "stock")
    private Integer stock;
    
    @Column(name = "min_order")
    private Integer minOrder;
    
    @Column(name = "max_order")
    private Integer maxOrder;
    
    @Column(name = "youtube_link")
	public String youtubeLink;
    
    @Column(name = "condition")
    private Integer condition;
    
    @Column(name = "created_time")
    public Timestamp createdTime;
    
    //jenis satuan (kg, m, dll)
    @Column(name = "unit_type")
    private Integer unitType;
    
    //nilai satuan
    @Column(name = "unit_value")
    private BigDecimal unitValue;
	
	@Column(name = "description")
    public String description;
    
    @Column(name = "shop_id")
	public Integer shopId;
	
	@Column(name = "shop_name")
	public String shopName;
	
	@Column(name = "priority")
	public Integer priority;
	
	@Column(name = "shop_ratting_count")
   	public BigInteger shopRattingCount;
    
    @Column(name = "shop_ratting_average")
   	public Double shopRattingAverage;
    
    @Column(name = "category_id")
    private Integer categoryId;
	
    @Column(name = "category_name")
	public String categoryName;
    
    @Column(name = "sub_category_id")
    private Integer subCategoryId;
	
    @Column(name = "sub_category_name")
	public String subCategoryName;
    
    @Transient
    List<ProductType> productTypeList;
    
    @Transient
    List<Document> documentList;
    
	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
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

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(Double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public BigInteger getProductRattingCount() {
		return productRattingCount;
	}

	public void setProductRattingCount(BigInteger productRattingCount) {
		this.productRattingCount = productRattingCount;
	}

	public Double getProductRattingAverage() {
		return productRattingAverage;
	}

	public void setProductRattingAverage(Double productRattingAverage) {
		this.productRattingAverage = productRattingAverage;
	}

	public BigDecimal getSize() {
		return size;
	}

	public void setSize(BigDecimal size) {
		this.size = size;
	}

	public Integer getSold() {
		return sold;
	}

	public void setSold(Integer sold) {
		this.sold = sold;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
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

	public String getYoutubeLink() {
		return youtubeLink;
	}

	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public BigInteger getShopRattingCount() {
		return shopRattingCount;
	}

	public void setShopRattingCount(BigInteger shopRattingCount) {
		this.shopRattingCount = shopRattingCount;
	}

	public Double getShopRattingAverage() {
		return shopRattingAverage;
	}

	public void setShopRattingAverage(Double shopRattingAverage) {
		this.shopRattingAverage = shopRattingAverage;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Integer getSubCategoryId() {
		return subCategoryId;
	}

	public void setSubCategoryId(Integer subCategoryId) {
		this.subCategoryId = subCategoryId;
	}

	public String getSubCategoryName() {
		return subCategoryName;
	}

	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

	public List<ProductType> getProductTypeList() {
		return productTypeList;
	}

	public void setProductTypeList(List<ProductType> productTypeList) {
		this.productTypeList = productTypeList;
	}

	public List<Document> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<Document> documentList) {
		this.documentList = documentList;
	}

	public Integer getCondition() {
		return condition;
	}

	public void setCondition(Integer condition) {
		this.condition = condition;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}
	
}
