package com.cartas.jaktani.dto;

public class PaymentChargeRequest {
    private String paymentType;
    private String orderId;
    private String grossAmount;
    private String bank;
    private Integer userID;

    public PaymentChargeRequest() {
    }

    public PaymentChargeRequest(String paymentType, String orderId, String grossAmount, String bank, Integer userID) {
        this.paymentType = paymentType;
        this.orderId = orderId;
        this.grossAmount = grossAmount;
        this.bank = bank;
        this.userID = userID;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(String grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentChargeRequest{");
        sb.append("paymentType='").append(paymentType).append('\'');
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", grossAmount='").append(grossAmount).append('\'');
        sb.append(", bank='").append(bank).append('\'');
        sb.append(", userID=").append(userID);
        sb.append('}');
        return sb.toString();
    }
}
