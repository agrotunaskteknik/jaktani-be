package com.cartas.jaktani.dto;

public class RajaOngkirCitySingle {
    private QueryBody query;
    private CityBody results;
    private RajaOngkirStatus status;

    public RajaOngkirCitySingle(QueryBody query, CityBody results, RajaOngkirStatus status) {
        this.query = query;
        this.results = results;
        this.status = status;
    }

    public QueryBody getQuery() {
        return query;
    }

    public void setQuery(QueryBody query) {
        this.query = query;
    }

    public CityBody getResults() {
        return results;
    }

    public void setResults(CityBody results) {
        this.results = results;
    }

    public RajaOngkirStatus getStatus() {
        return status;
    }

    public void setStatus(RajaOngkirStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RajaOngkirCitySingle{");
        sb.append("query=").append(query);
        sb.append(", results=").append(results);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
