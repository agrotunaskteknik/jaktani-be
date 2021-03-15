package com.cartas.jaktani.dto;

public class PaymentGatewayDetail {
    private String bank;

    public PaymentGatewayDetail(String bank) {
        this.bank = bank;
    }

    public PaymentGatewayDetail() {
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentGatewayDetail{");
        sb.append("bank='").append(bank).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
