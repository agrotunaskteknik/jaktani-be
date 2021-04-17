package com.cartas.jaktani.dto;

public class VerifyOrderShippingRequest {
    private String resiCode;
    private Long orderID;
    private Long shopID;

    public VerifyOrderShippingRequest() {
    }

    public VerifyOrderShippingRequest(String resiCode, Long orderID, Long shopID) {
        this.resiCode = resiCode;
        this.orderID = orderID;
        this.shopID = shopID;
    }

    public String getResiCode() {
        return resiCode;
    }

    public void setResiCode(String resiCode) {
        this.resiCode = resiCode;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public Long getShopID() {
        return shopID;
    }

    public void setShopID(Long shopID) {
        this.shopID = shopID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VerifyOrderRequest{");
        sb.append("resiCode='").append(resiCode).append('\'');
        sb.append(", orderID=").append(orderID);
        sb.append(", shopID=").append(shopID);
        sb.append('}');
        return sb.toString();
    }
}
