package com.cartas.jaktani.model;

import javax.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "document")
public class Document {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	@Column(name = "refference_id")
    private Integer refferenceId;
	@Column(name = "name")
    public String name;
	@Column(name = "type")
    public Integer type;
	@Column(name = "code")
    public Integer code;
	@Column(name = "format")
    public String format;
	@Column(name = "content_data")
    public String contentData;
	@Column(name = "size")
    public String size;
	@Column(name = "created_time")
    public Timestamp createdTime;
	@Column(name = "updated_time")
    public Timestamp updatedTime;
	@Column(name = "status")
    public Integer status;
	@Column(name = "order_number")
    public Integer orderNumber;

	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getRefferenceId() {
		return refferenceId;
	}
	public void setRefferenceId(Integer refferenceId) {
		this.refferenceId = refferenceId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getContentData() {
		return contentData;
	}
	public void setContentData(String contentData) {
		this.contentData = contentData;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
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
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getOrderNumber() {
		return orderNumber;
	}
	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	
	
	 
}
