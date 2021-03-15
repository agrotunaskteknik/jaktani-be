package com.cartas.jaktani.dto;

public class CityBody {
    private String province_id;
    private String province;
    private String city_id;
    private String city_name;
    private String type;
    private String postal_code;

    public CityBody(String province_id, String province, String city_id, String city_name, String type, String postal_code) {
        this.province_id = province_id;
        this.province = province;
        this.city_id = city_id;
        this.city_name = city_name;
        this.type = type;
        this.postal_code = postal_code;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CityBody{");
        sb.append("province_id='").append(province_id).append('\'');
        sb.append(", province='").append(province).append('\'');
        sb.append(", city_id='").append(city_id).append('\'');
        sb.append(", city_name='").append(city_name).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", postal_code='").append(postal_code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
