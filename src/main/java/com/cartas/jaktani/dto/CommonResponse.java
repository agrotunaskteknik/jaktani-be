package com.cartas.jaktani.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("error_message")
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
