package com.cartas.jaktani.dto;

public class PaymentGatewayDto {
    private String paymentType;
    private String paymentImage;
    private String paymentDescription;
    private String paymentTitle;
    private PaymentGatewayDetail bankTransfer;

    public PaymentGatewayDto() {
    }

    public PaymentGatewayDto(String paymentType, String paymentImage, String paymentDescription, String paymentTitle, PaymentGatewayDetail bankTransfer) {
        this.paymentType = paymentType;
        this.paymentImage = paymentImage;
        this.paymentDescription = paymentDescription;
        this.paymentTitle = paymentTitle;
        this.bankTransfer = bankTransfer;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentImage() {
        return paymentImage;
    }

    public void setPaymentImage(String paymentImage) {
        this.paymentImage = paymentImage;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public String getPaymentTitle() {
        return paymentTitle;
    }

    public void setPaymentTitle(String paymentTitle) {
        this.paymentTitle = paymentTitle;
    }

    public PaymentGatewayDetail getBankTransfer() {
        return bankTransfer;
    }

    public void setBankTransfer(PaymentGatewayDetail bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentGatewayDto{");
        sb.append("paymentType='").append(paymentType).append('\'');
        sb.append(", paymentImage='").append(paymentImage).append('\'');
        sb.append(", paymentDescription='").append(paymentDescription).append('\'');
        sb.append(", paymentTitle='").append(paymentTitle).append('\'');
        sb.append(", bankTransfer=").append(bankTransfer);
        sb.append('}');
        return sb.toString();
    }
}
