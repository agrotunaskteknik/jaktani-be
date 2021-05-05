package com.cartas.jaktani.dto;

public class QueryWaybill {
    private String waybill;
    private String courier;


    // Getter Methods

    public String getWaybill() {
        return waybill;
    }

    public String getCourier() {
        return courier;
    }

    // Setter Methods

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("QueryWaybill{");
        sb.append("waybill='").append(waybill).append('\'');
        sb.append(", courier='").append(courier).append('\'');
        sb.append('}');
        return sb.toString();
    }
}