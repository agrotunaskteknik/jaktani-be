package com.cartas.jaktani.dto;

import java.util.List;

public class ProvinceParent {
    private RajaOngkir rajaongkir;

    public ProvinceParent(RajaOngkir rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    public RajaOngkir getRajaongkir() {
        return rajaongkir;
    }

    public void setRajaongkir(RajaOngkir rajaongkir) {
        this.rajaongkir = rajaongkir;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProvinceParent{");
        sb.append("rajaongkir=").append(rajaongkir);
        sb.append('}');
        return sb.toString();
    }
}

class RajaOngkir {
    private List<QueryBody> query;
    private List<ProvinceBody> results;
    private RajaOngkirStatus status;

    public RajaOngkir(List<QueryBody> query, List<ProvinceBody> results, RajaOngkirStatus status) {
        this.query = query;
        this.results = results;
        this.status = status;
    }

    public List<QueryBody> getQuery() {
        return query;
    }

    public void setQuery(List<QueryBody> query) {
        this.query = query;
    }

    public List<ProvinceBody> getResults() {
        return results;
    }

    public void setResults(List<ProvinceBody> results) {
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
        final StringBuilder sb = new StringBuilder("RajaOngkir{");
        sb.append("query=").append(query);
        sb.append(", results=").append(results);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}

class QueryBody {
    private String id;

    public QueryBody(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryBody{");
        sb.append("id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

class RajaOngkirStatus {
    private Integer code;
    private String description;

    public RajaOngkirStatus(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("status{");
        sb.append("code=").append(code);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

class ProvinceBody {
    private String province_id;
    private String province;

    public ProvinceBody(String province_id, String province) {
        this.province_id = province_id;
        this.province = province;
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ProvinceBody{");
        sb.append("province_id='").append(province_id).append('\'');
        sb.append(", province='").append(province).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
