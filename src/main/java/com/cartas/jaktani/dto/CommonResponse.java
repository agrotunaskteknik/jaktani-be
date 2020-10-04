package com.cartas.jaktani.dto;

public class CommonResponse {
    private String errorMessage;
    private String status;
    private String message;

    public CommonResponse() {
    }

    public CommonResponse(String errorMessage, String status, String message) {
        this.errorMessage = errorMessage;
        this.status = status;
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
