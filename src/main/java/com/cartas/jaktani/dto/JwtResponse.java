package com.cartas.jaktani.dto;

import java.io.Serializable;

public class JwtResponse implements Serializable {
    private static final long serialVersionUID = -8091879091924046844L;
    private final String jwttoken;
    private Long lastRequest;
    private Long expiredAt;
    private String refreshToken;


    public JwtResponse(String jwttoken) {
        this.jwttoken = jwttoken;
    }

    public JwtResponse(String jwttoken, Long lastRequest, Long expiredAt, String refreshToken) {
        this.jwttoken = jwttoken;
        this.lastRequest = lastRequest;
        this.expiredAt = expiredAt;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return this.jwttoken;
    }

    public Long getLastRequest() {
        return lastRequest;
    }

    public void setLastRequest(Long lastRequest) {
        this.lastRequest = lastRequest;
    }

    public Long getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(Long expiredAt) {
        this.expiredAt = expiredAt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
