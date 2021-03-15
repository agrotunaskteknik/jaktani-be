package com.cartas.jaktani.dto;

public class ProvinceDto {
    private String provinceId;
    private String provinceName;

    public ProvinceDto() {
    }

    public ProvinceDto(String provinceId, String provinceName) {
        this.provinceId = provinceId;
        this.provinceName = provinceName;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProvinceDto{");
        sb.append("provinceId='").append(provinceId).append('\'');
        sb.append(", provinceName='").append(provinceName).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
