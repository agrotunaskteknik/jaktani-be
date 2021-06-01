package com.cartas.jaktani.dto;

public class LoginGoogleRequest {
    private String googleJwtToken;

    public LoginGoogleRequest() {
    }

    public LoginGoogleRequest(String googleJwtToken) {
        this.googleJwtToken = googleJwtToken;
    }

    public String getGoogleJwtToken() {
        return googleJwtToken;
    }

    public void setGoogleJwtToken(String googleJwtToken) {
        this.googleJwtToken = googleJwtToken;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("LoginGoogleRequest{");
        sb.append("googleJwtToken='").append(googleJwtToken).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
