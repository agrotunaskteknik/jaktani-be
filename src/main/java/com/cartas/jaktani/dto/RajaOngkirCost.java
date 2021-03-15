package com.cartas.jaktani.dto;

import java.util.List;

public class RajaOngkirCost {
    private QueryBody query;
    private List<CostResult> results;
    private RajaOngkirStatus status;
    private CityBody origin_details;
    private CityBody destination_details;

    public RajaOngkirCost(QueryBody query, List<CostResult> results, RajaOngkirStatus status, CityBody origin_details, CityBody destination_details) {
        this.query = query;
        this.results = results;
        this.status = status;
        this.origin_details = origin_details;
        this.destination_details = destination_details;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RajaOngkirCost{");
        sb.append("query=").append(query);
        sb.append(", results=").append(results);
        sb.append(", status=").append(status);
        sb.append(", origin_details=").append(origin_details);
        sb.append(", destination_details=").append(destination_details);
        sb.append('}');
        return sb.toString();
    }

    public QueryBody getQuery() {
        return query;
    }

    public void setQuery(QueryBody query) {
        this.query = query;
    }

    public List<CostResult> getResults() {
        return results;
    }

    public void setResults(List<CostResult> results) {
        this.results = results;
    }

    public RajaOngkirStatus getStatus() {
        return status;
    }

    public void setStatus(RajaOngkirStatus status) {
        this.status = status;
    }

    public CityBody getOrigin_details() {
        return origin_details;
    }

    public void setOrigin_details(CityBody origin_details) {
        this.origin_details = origin_details;
    }

    public CityBody getDestination_details() {
        return destination_details;
    }

    public void setDestination_details(CityBody destination_details) {
        this.destination_details = destination_details;
    }
}
