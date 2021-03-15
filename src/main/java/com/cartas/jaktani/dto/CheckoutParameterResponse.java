package com.cartas.jaktani.dto;

public class CheckoutParameterResponse {
    private Long grossAmount;
    private Long customerId;
    private Long orderId;

    public CheckoutParameterResponse() {
    }

    public CheckoutParameterResponse(Long grossAmount, Long customerId, Long orderId) {
        this.grossAmount = grossAmount;
        this.customerId = customerId;
        this.orderId = orderId;
    }

    public Long getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Long grossAmount) {
        this.grossAmount = grossAmount;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckoutParameterResponse{");
        sb.append("grossAmount=").append(grossAmount);
        sb.append(", customerId=").append(customerId);
        sb.append(", orderId=").append(orderId);
        sb.append('}');
        return sb.toString();
    }
}
