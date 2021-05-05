package com.cartas.jaktani.dto;

public class DetailsWaybill {
    private String waybill_number;
    private String waybill_date;
    private String waybill_time;
    private String weight;
    private String origin;
    private String destination;
    private String shippper_name;
    private String shipper_address1;
    private String shipper_address2;
    private String shipper_address3;
    private String shipper_city;
    private String receiver_name;
    private String receiver_address1;
    private String receiver_address2;
    private String receiver_address3;
    private String receiver_city;


    // Getter Methods

    public String getWaybill_number() {
        return waybill_number;
    }

    public String getWaybill_date() {
        return waybill_date;
    }

    public String getWaybill_time() {
        return waybill_time;
    }

    public String getWeight() {
        return weight;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getShippper_name() {
        return shippper_name;
    }

    public String getShipper_address1() {
        return shipper_address1;
    }

    public String getShipper_address2() {
        return shipper_address2;
    }

    public String getShipper_address3() {
        return shipper_address3;
    }

    public String getShipper_city() {
        return shipper_city;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public String getReceiver_address1() {
        return receiver_address1;
    }

    public String getReceiver_address2() {
        return receiver_address2;
    }

    public String getReceiver_address3() {
        return receiver_address3;
    }

    public String getReceiver_city() {
        return receiver_city;
    }

    // Setter Methods

    public void setWaybill_number(String waybill_number) {
        this.waybill_number = waybill_number;
    }

    public void setWaybill_date(String waybill_date) {
        this.waybill_date = waybill_date;
    }

    public void setWaybill_time(String waybill_time) {
        this.waybill_time = waybill_time;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setShippper_name(String shippper_name) {
        this.shippper_name = shippper_name;
    }

    public void setShipper_address1(String shipper_address1) {
        this.shipper_address1 = shipper_address1;
    }

    public void setShipper_address2(String shipper_address2) {
        this.shipper_address2 = shipper_address2;
    }

    public void setShipper_address3(String shipper_address3) {
        this.shipper_address3 = shipper_address3;
    }

    public void setShipper_city(String shipper_city) {
        this.shipper_city = shipper_city;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public void setReceiver_address1(String receiver_address1) {
        this.receiver_address1 = receiver_address1;
    }

    public void setReceiver_address2(String receiver_address2) {
        this.receiver_address2 = receiver_address2;
    }

    public void setReceiver_address3(String receiver_address3) {
        this.receiver_address3 = receiver_address3;
    }

    public void setReceiver_city(String receiver_city) {
        this.receiver_city = receiver_city;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DetailsWaybill{");
        sb.append("waybill_number='").append(waybill_number).append('\'');
        sb.append(", waybill_date='").append(waybill_date).append('\'');
        sb.append(", waybill_time='").append(waybill_time).append('\'');
        sb.append(", weight='").append(weight).append('\'');
        sb.append(", origin='").append(origin).append('\'');
        sb.append(", destination='").append(destination).append('\'');
        sb.append(", shippper_name='").append(shippper_name).append('\'');
        sb.append(", shipper_address1='").append(shipper_address1).append('\'');
        sb.append(", shipper_address2='").append(shipper_address2).append('\'');
        sb.append(", shipper_address3='").append(shipper_address3).append('\'');
        sb.append(", shipper_city='").append(shipper_city).append('\'');
        sb.append(", receiver_name='").append(receiver_name).append('\'');
        sb.append(", receiver_address1='").append(receiver_address1).append('\'');
        sb.append(", receiver_address2='").append(receiver_address2).append('\'');
        sb.append(", receiver_address3='").append(receiver_address3).append('\'');
        sb.append(", receiver_city='").append(receiver_city).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
