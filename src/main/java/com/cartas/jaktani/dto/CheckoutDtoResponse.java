package com.cartas.jaktani.dto;


public class CheckoutDtoResponse extends CommonResponse {
    private CheckoutDtoData data;

    public CheckoutDtoResponse() {
    }

    public CheckoutDtoResponse(CheckoutDtoData data) {
        this.data = data;
    }

    public CheckoutDtoResponse(String errorMessage, String status, String message, CheckoutDtoData data) {
        super(errorMessage, status, message);
        this.data = data;
    }

    public CheckoutDtoData getData() {
        return data;
    }

    public void setData(CheckoutDtoData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckoutDtoResponse{");
        sb.append("data=").append(data);
        sb.append('}');
        return sb.toString();
    }
}
