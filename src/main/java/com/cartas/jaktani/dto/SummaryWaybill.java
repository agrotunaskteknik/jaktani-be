package com.cartas.jaktani.dto;

public class SummaryWaybill {
    private String courier_code;
    private String courier_name;
    private String waybill_number;
    private String service_code;
    private String waybill_date;
    private String shipper_name;
    private String receiver_name;
    private String origin;
    private String destination;
    private String status;


    // Getter Methods

    public String getCourier_code() {
        return courier_code;
    }

    public String getCourier_name() {
        return courier_name;
    }

    public String getWaybill_number() {
        return waybill_number;
    }

    public String getService_code() {
        return service_code;
    }

    public String getWaybill_date() {
        return waybill_date;
    }

    public String getShipper_name() {
        return shipper_name;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getStatus() {
        return status;
    }

    // Setter Methods

    public void setCourier_code(String courier_code) {
        this.courier_code = courier_code;
    }

    public void setCourier_name(String courier_name) {
        this.courier_name = courier_name;
    }

    public void setWaybill_number(String waybill_number) {
        this.waybill_number = waybill_number;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public void setWaybill_date(String waybill_date) {
        this.waybill_date = waybill_date;
    }

    public void setShipper_name(String shipper_name) {
        this.shipper_name = shipper_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SummaryWaybill{");
        sb.append("courier_code='").append(courier_code).append('\'');
        sb.append(", courier_name='").append(courier_name).append('\'');
        sb.append(", waybill_number='").append(waybill_number).append('\'');
        sb.append(", service_code='").append(service_code).append('\'');
        sb.append(", waybill_date='").append(waybill_date).append('\'');
        sb.append(", shipper_name='").append(shipper_name).append('\'');
        sb.append(", receiver_name='").append(receiver_name).append('\'');
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
