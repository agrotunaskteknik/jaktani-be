package com.cartas.jaktani.dto;

public class RemoveCartDto {
    private Long cartID;
    private Long userID;

    public RemoveCartDto() {
    }

    public RemoveCartDto(Long cartID, Long userID) {
        this.cartID = cartID;
        this.userID = userID;
    }

    public Long getCartID() {
        return cartID;
    }

    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RemoveCartDto{");
        sb.append("cartID=").append(cartID);
        sb.append(", userID=").append(userID);
        sb.append('}');
        return sb.toString();
    }
}
