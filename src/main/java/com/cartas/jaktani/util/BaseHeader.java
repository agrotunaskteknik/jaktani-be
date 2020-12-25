package com.cartas.jaktani.util;

public class BaseHeader {
    private String token;
    private String lang;

    public BaseHeader() {
    }

    public BaseHeader(String token, String lang) {
        this.token = token;
        this.lang = lang;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
