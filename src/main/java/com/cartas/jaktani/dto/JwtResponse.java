package com.cartas.jaktani.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private String accessToken;
    private Long lastRequest;
    private Long expiredAt;
    private String refreshToken;

    public JwtResponse() {
    }

    public JwtResponse(String accessToken) {
        this.accessToken = accessToken;
    }

    public JwtResponse(String accessToken, Long lastRequest, Long expiredAt, String refreshToken) {
        this.accessToken = accessToken;
        this.lastRequest = lastRequest;
        this.expiredAt = expiredAt;
        this.refreshToken = refreshToken;
    }

    @JsonProperty("access_token")
    public String getAccessToken() {
        return this.accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("last_request")
    public Long getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Long lastRequest) {
        this.lastRequest = lastRequest;
    }

    @JsonProperty("expired_at")
    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    @JsonProperty("refresh_token")
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
