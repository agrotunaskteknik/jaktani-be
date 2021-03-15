package com.cartas.jaktani.dto;

public class AddressDetailDto {
    private String addressId;
    private String provinceId;
    private String description;
    private String cityId;
    private String provinceName;
    private String cityName;
    private String postalCode;
    private Integer type;
    private Integer relationId;
    private Integer status;


    public AddressDetailDto() {
    }

    public AddressDetailDto(String addressId, String provinceId, String description, String cityId, String provinceName, String cityName, String postalCode, Integer type, Integer relationId, Integer status) {
        this.addressId = addressId;
        this.provinceId = provinceId;
        this.description = description;
        this.cityId = cityId;
        this.provinceName = provinceName;
        this.cityName = cityName;
        this.postalCode = postalCode;
        this.type = type;
        this.relationId = relationId;
        this.status = status;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddressDetailDto{");
        sb.append("addressId='").append(addressId).append('\'');
        sb.append(", provinceId='").append(provinceId).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", cityId='").append(cityId).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append(", type=").append(type);
        sb.append(", relationId=").append(relationId);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
