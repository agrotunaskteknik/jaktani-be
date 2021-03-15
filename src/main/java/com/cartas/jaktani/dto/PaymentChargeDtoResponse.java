package com.cartas.jaktani.dto;

import java.util.List;

public class PaymentChargeDtoResponse extends CommonResponse{
    private String statusMessage;
    private String transactionId;
    private String orderId;
    private String merchantId;
    private Long grossAmount;
    private String grossAmountFmt;
    private String currency;
    private String paymentType;
    private Long transactionTimeInMilis;
    private String transactionStatus;
    private List<VaNumberDto> vaNumberDto;
    private String fraudStatus;

    public PaymentChargeDtoResponse() {
    }

    public PaymentChargeDtoResponse(String statusMessage, String transactionId, String orderId, String merchantId, Long grossAmount, String grossAmountFmt, String currency, String paymentType, Long transactionTimeInMilis, String transactionStatus, List<VaNumberDto> vaNumberDto, String fraudStatus) {
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.grossAmount = grossAmount;
        this.grossAmountFmt = grossAmountFmt;
        this.currency = currency;
        this.paymentType = paymentType;
        this.transactionTimeInMilis = transactionTimeInMilis;
        this.transactionStatus = transactionStatus;
        this.vaNumberDto = vaNumberDto;
        this.fraudStatus = fraudStatus;
    }

    public PaymentChargeDtoResponse(String errorMessage, String status, String message, String statusMessage, String transactionId, String orderId, String merchantId, Long grossAmount, String grossAmountFmt, String currency, String paymentType, Long transactionTimeInMilis, String transactionStatus, List<VaNumberDto> vaNumberDto, String fraudStatus) {
        super(errorMessage, status, message);
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.merchantId = merchantId;
        this.grossAmount = grossAmount;
        this.grossAmountFmt = grossAmountFmt;
        this.currency = currency;
        this.paymentType = paymentType;
        this.transactionTimeInMilis = transactionTimeInMilis;
        this.transactionStatus = transactionStatus;
        this.vaNumberDto = vaNumberDto;
        this.fraudStatus = fraudStatus;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public Long getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Long grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getGrossAmountFmt() {
        return grossAmountFmt;
    }

    public void setGrossAmountFmt(String grossAmountFmt) {
        this.grossAmountFmt = grossAmountFmt;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getTransactionTimeInMilis() {
        return transactionTimeInMilis;
    }

    public void setTransactionTimeInMilis(Long transactionTimeInMilis) {
        this.transactionTimeInMilis = transactionTimeInMilis;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public List<VaNumberDto> getVaNumberDto() {
        return vaNumberDto;
    }

    public void setVaNumberDto(List<VaNumberDto> vaNumberDto) {
        this.vaNumberDto = vaNumberDto;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public void setFraudStatus(String fraudStatus) {
        this.fraudStatus = fraudStatus;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentChargeDtoResponse{");
        sb.append("statusMessage='").append(statusMessage).append('\'');
        sb.append(", transactionId='").append(transactionId).append('\'');
        sb.append(", orderId='").append(orderId).append('\'');
        sb.append(", merchantId='").append(merchantId).append('\'');
        sb.append(", grossAmount=").append(grossAmount);
        sb.append(", grossAmountFmt='").append(grossAmountFmt).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", paymentType='").append(paymentType).append('\'');
        sb.append(", transactionTimeInMilis=").append(transactionTimeInMilis);
        sb.append(", transactionStatus='").append(transactionStatus).append('\'');
        sb.append(", vaNumberDto=").append(vaNumberDto);
        sb.append(", fraudStatus='").append(fraudStatus).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
