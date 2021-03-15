package com.cartas.jaktani.dto;

public class CityDto {
    private String cityId;
    private String provinceId;
    private String provinceName;
    private String type;
    private String cityName;
    private String postalCode;

    public CityDto() {
    }

    public CityDto(String cityId, String provinceId, String provinceName, String type, String cityName, String postalCode) {
        this.cityId = cityId;
        this.provinceId = provinceId;
        this.provinceName = provinceName;
        this.type = type;
        this.cityName = cityName;
        this.postalCode = postalCode;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CityDto{");
        sb.append("cityId='").append(cityId).append('\'');
        sb.append(", provinceId='").append(provinceId).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", cityName='").append(cityName).append('\'');
        sb.append(", postalCode='").append(postalCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
