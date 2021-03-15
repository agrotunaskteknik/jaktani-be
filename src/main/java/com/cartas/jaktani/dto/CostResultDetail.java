package com.cartas.jaktani.dto;

import java.util.List;

public class CostResultDetail {
    private String service;
    private String description;
    private List<CostResultDetailData> cost;

    public CostResultDetail(String service, String description, List<CostResultDetailData> cost) {
        this.service = service;
        this.description = description;
        this.cost = cost;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CostResultDetail{");
        sb.append("service='").append(service).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", cost=").append(cost);
        sb.append('}');
        return sb.toString();
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<CostResultDetailData> getCost() {
        return cost;
    }

    public void setCost(List<CostResultDetailData> cost) {
        this.cost = cost;
    }
}

