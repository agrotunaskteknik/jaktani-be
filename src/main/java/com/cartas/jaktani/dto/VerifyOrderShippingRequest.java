package com.cartas.jaktani.dto;

public class VerifyOrderShippingRequest {
    private String resiCode;
    private Long orderID;
    private Long shopID;
    private Integer status;

    public VerifyOrderShippingRequest() {
    }

    public VerifyOrderShippingRequest(String resiCode, Long orderID, Long shopID, Integer status) {
        this.resiCode = resiCode;
        this.orderID = orderID;
        this.shopID = shopID;
        this.status = status;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VerifyOrderShippingRequest{");
        sb.append("resiCode='").append(resiCode).append('\'');
        sb.append(", orderID=").append(orderID);
        sb.append(", shopID=").append(shopID);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
