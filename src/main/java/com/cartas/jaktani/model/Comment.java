package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "comment")
public class Comment {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	
	@Column(name = "product_id")
	public Integer productId;
	
	@Column(name = "created_by")
	public Integer createdBy;
	
	@Column(name = "comment")
    public String comment;
	
	@Column(name = "status")
    public Integer status;
	
	@Column(name = "created_time")
    public Timestamp createdTime;
	
	@Column(name = "shop_id")
	public Integer shopId;

	
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

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	
	
    
}
