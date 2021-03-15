package com.cartas.jaktani.dto;

import java.util.List;

public class CityParent {
    private RajaOngkirCity rajaongkir;

    public CityParent(RajaOngkirCity rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    public RajaOngkirCity getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(RajaOngkirCity rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CityParent{");
        sb.append("rajaongkir=").append(rajaongkir);
        sb.append('}');
        return sb.toString();
    }
}

class RajaOngkirCity {
    private QueryBody query;
    private List<CityBody> results;
    private RajaOngkirStatus status;

    public RajaOngkirCity(QueryBody query, List<CityBody> results, RajaOngkirStatus status) {
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

    public List<CityBody> getResults() {
        return results;
    }

    public void setResults(List<CityBody> results) {
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
        final StringBuilder sb = new StringBuilder("RajaOngkirCity{");
        sb.append("query=").append(query);
        sb.append(", results=").append(results);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}


