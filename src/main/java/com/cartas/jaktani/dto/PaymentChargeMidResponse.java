package com.cartas.jaktani.dto;

import java.util.List;

public class PaymentChargeMidResponse {
    private String status_code;
    private String status_message;
    private String transaction_id;
    private String order_id;
    private String merchant_id;
    private String gross_amount;
    private String currency;
    private String payment_type;
    private String transaction_time;
    private String transaction_status;
    private List<VaNumber> va_numbers;
    private String fraud_status;
    private String signature_key;

    public PaymentChargeMidResponse() {
    }

    public PaymentChargeMidResponse(String signature_key, String status_code, String status_message, String transaction_id, String order_id, String merchant_id, String gross_amount, String currency, String payment_type, String transaction_time, String transaction_status, List<VaNumber> va_numbers, String fraud_status) {
        this.status_code = status_code;
        this.status_message = status_message;
        this.transaction_id = transaction_id;
        this.order_id = order_id;
        this.merchant_id = merchant_id;
        this.gross_amount = gross_amount;
        this.currency = currency;
        this.payment_type = payment_type;
        this.transaction_time = transaction_time;
        this.transaction_status = transaction_status;
        this.va_numbers = va_numbers;
        this.fraud_status = fraud_status;
        this.signature_key = signature_key;
    }

    public String getSignature_key() {
        return signature_key;
    }

    public void setSignature_key(String signature_key) {
        this.signature_key = signature_key;
    }

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getMerchant_id() {
        return merchant_id;
    }

    public void setMerchant_id(String merchant_id) {
        this.merchant_id = merchant_id;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getTransaction_time() {
        return transaction_time;
    }

    public void setTransaction_time(String transaction_time) {
        this.transaction_time = transaction_time;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public List<VaNumber> getVa_numbers() {
        return va_numbers;
    }

    public void setVa_numbers(List<VaNumber> va_numbers) {
        this.va_numbers = va_numbers;
    }

    public String getFraud_status() {
        return fraud_status;
    }

    public void setFraud_status(String fraud_status) {
        this.fraud_status = fraud_status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentChargeMidResponse{");
        sb.append("status_code='").append(status_code).append('\'');
        sb.append(", status_message='").append(status_message).append('\'');
        sb.append(", transaction_id='").append(transaction_id).append('\'');
        sb.append(", order_id='").append(order_id).append('\'');
        sb.append(", merchant_id='").append(merchant_id).append('\'');
        sb.append(", gross_amount='").append(gross_amount).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", payment_type='").append(payment_type).append('\'');
        sb.append(", transaction_time='").append(transaction_time).append('\'');
        sb.append(", transaction_status='").append(transaction_status).append('\'');
        sb.append(", va_numbers=").append(va_numbers);
        sb.append(", fraud_status='").append(fraud_status).append('\'');
        sb.append(", signature_key='").append(signature_key).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
