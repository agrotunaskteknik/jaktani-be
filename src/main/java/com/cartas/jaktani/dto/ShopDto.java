package com.cartas.jaktani.dto;
import java.sql.Timestamp;

import javax.persistence.Column;

public class ShopDto {
	public Integer id;
	public Integer userID;
    public String name;
    public String address;
    public Integer updatedBy;
    public Integer status;
    public String description;
    public Integer priority;
    public String latitude;
    public String longitude;
    public String logoFilePath;
    public String logoUrlPath;
    public String logoFilePathHome;
    public String logoUrlPathHome;
    public String logoBase64;
    public UserDto userDto;
    
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
	public String getLogoBase64() {
		return logoBase64;
	}
	public void setLogoBase64(String logoBase64) {
		this.logoBase64 = logoBase64;
	}
	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
