package com.cartas.jaktani.model;

import javax.persistence.*;

import java.sql.Timestamp;


@Entity
@Table(name = "shop")
public class Shop {
	@Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    public Integer id;
	
	@Column(name = "user_id")
    private Integer userID;
	
	@Column(name = "name")
    public String name;
	
	@Column(name = "address")
    public String address;
	
	@Column(name = "updated_by")
    public Integer updatedBy;
	
	@Column(name = "created_time")
    public Timestamp createdTime;
	
	@Column(name = "updated_time")
    public Timestamp updatedTime;
	
	@Column(name = "status")
    public Integer status;
	
	@Column(name = "description")
    public String description;
	
	@Column(name = "priority")
    public Integer priority;
	
	@Column(name = "latitude")
    public String latitude;
	
	@Column(name = "longitude")
    public String longitude;

	@Column(name = "logo_file_path")
    public String logoFilePath;
	
	@Column(name = "logo_url_path")
    public String logoUrlPath;
	
	@Column(name = "logo_file_path_home")
    public String logoFilePathHome;
	
	@Column(name = "logo_url_path_home")
    public String logoUrlPathHome;
	
	@Transient
	public Users user;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getUserID() {
		return userID;
	}
	public void setUserID(Integer userID) {
		this.userID = userID;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
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
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	
	public String getLogoFilePath() {
		return logoFilePath;
	}
	public void setLogoFilePath(String logoFilePath) {
		this.logoFilePath = logoFilePath;
	}
	
	public String getLogoUrlPath() {
		return logoUrlPath;
	}
	public void setLogoUrlPath(String logoUrlPath) {
		this.logoUrlPath = logoUrlPath;
	}
	
	public String getLogoFilePathHome() {
		return logoFilePathHome;
	}
	public void setLogoFilePathHome(String logoFilePathHome) {
		this.logoFilePathHome = logoFilePathHome;
	}
	
	public String getLogoUrlPathHome() {
		return logoUrlPathHome;
	}
	public void setLogoUrlPathHome(String logoUrlPathHome) {
		this.logoUrlPathHome = logoUrlPathHome;
	}
	public Users getUser() {
		return user;
	}
	public void setUser(Users user) {
		this.user = user;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
	 
}
