package com.cartas.jaktani.dto;

import java.util.List;

public class CostResult {
    private String code;
    private String name;
    private String city_id;
    private List<CostResultDetail> costs;

    public CostResult(String code, String name, String city_id, List<CostResultDetail> costs) {
        this.code = code;
        this.name = name;
        this.city_id = city_id;
        this.costs = costs;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CostResult{");
        sb.append("code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", city_id='").append(city_id).append('\'');
        sb.append(", costs=").append(costs);
        sb.append('}');
        return sb.toString();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public List<CostResultDetail> getCosts() {
        return costs;
    }

    public void setCosts(List<CostResultDetail> costs) {
        this.costs = costs;
    }
}

