package com.cartas.jaktani.dto;

public class CartListDtoRequest {
    private Long userID;

    public CartListDtoRequest() {
    }

    public CartListDtoRequest(Long userID) {
        this.userID = userID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CartListDtoRequest{");
        sb.append("userID=").append(userID);
        sb.append('}');
        return sb.toString();
    }
}
