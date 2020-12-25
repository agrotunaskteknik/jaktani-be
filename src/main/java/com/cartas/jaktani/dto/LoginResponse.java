package com.cartas.jaktani.dto;


public class LoginResponse extends CommonResponse {
    private UserDto user;
    private JwtResponse token;

    public LoginResponse() {
    }

    public LoginResponse(String errorMessage, String status, String message) {
        super(errorMessage, status, message);
    }

    public LoginResponse(UserDto user, JwtResponse token) {
        this.user = user;
        this.token = token;
    }

    public LoginResponse(String errorMessage, String status, String message, UserDto user, JwtResponse token) {
        super(errorMessage, status, message);
        this.user = user;
        this.token = token;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public JwtResponse getToken() {
        return token;
    }

    public void setToken(JwtResponse token) {
        this.token = token;
    }
}
