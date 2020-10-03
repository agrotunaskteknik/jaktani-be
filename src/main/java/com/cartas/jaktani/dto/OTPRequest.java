package com.cartas.jaktani.dto;

import java.io.Serializable;

public class OTPRequest implements Serializable {
    private static final long serialVersionUID = 5926468583005150707L;

    private String username;
    private String email;
    private String mobileNumber;

    //need default constructor for JSON Parsing
    public OTPRequest() {

    }

    public OTPRequest(String username, String email, String mobileNumber) {
        this.setUsername(username);
        this.setEmail(email);
        this.setMobileNumber(mobileNumber);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String password) {
        this.email = password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}