package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "address")
public class Address {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	
	@Column(name = "type")
    private Integer type;
	
	@Column(name = "relation_id")
    public Integer relationId;
	
	@Column(name = "description")
    public String description;
	
	@Column(name = "updated_by")
    public Integer updatedBy;

	@Column(name = "created_by")
	public Integer createdBy;
	
	@Column(name = "created_time")
    public Timestamp createdTime;
	
	@Column(name = "updated_time")
    public Timestamp updatedTime;
	
	@Column(name = "city")
    public String city;
	
	@Column(name = "province")
    public String province;
	
	@Column(name = "country")
    public String  country;
	
	@Column(name = "district")
    public String district;
	
	@Column(name = "pos_code")
    public String postalCode;

	@Column(name = "village")
    public String village;
	
	@Column(name = "status")
    public Integer status;

	public Address() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRelationId() {
		return relationId;
	}

	public void setRelationId(Integer relationId) {
		this.relationId = relationId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getVillage() {
		return village;
	}

	public void setVillage(String village) {
		this.village = village;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Address{");
		sb.append("id=").append(id);
		sb.append(", type=").append(type);
		sb.append(", relation_id=").append(relationId);
		sb.append(", description='").append(description).append('\'');
		sb.append(", updatedBy=").append(updatedBy);
		sb.append(", createdBy=").append(createdBy);
		sb.append(", createdTime=").append(createdTime);
		sb.append(", updatedTime=").append(updatedTime);
		sb.append(", city='").append(city).append('\'');
		sb.append(", province='").append(province).append('\'');
		sb.append(", country='").append(country).append('\'');
		sb.append(", district='").append(district).append('\'');
		sb.append(", postalCode='").append(postalCode).append('\'');
		sb.append(", village='").append(village).append('\'');
		sb.append(", status=").append(status);
		sb.append('}');
		return sb.toString();
	}
}
