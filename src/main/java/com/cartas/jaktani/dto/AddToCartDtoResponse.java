package com.cartas.jaktani.dto;

public class AddToCartDtoResponse extends CommonResponse{
    private Long cartCounter;

    public AddToCartDtoResponse() {
    }

    public AddToCartDtoResponse(String errorMessage, String status, String message) {
        super(errorMessage, status, message);
    }

    public AddToCartDtoResponse(Long cartCounter) {
        this.cartCounter = cartCounter;
    }

    public AddToCartDtoResponse(String errorMessage, String status, String message, Long cartCounter) {
        super(errorMessage, status, message);
        this.cartCounter = cartCounter;
    }

    public Long getCartCounter() {
        return cartCounter;
    }

    public void setCartCounter(Long cartCounter) {
        this.cartCounter = cartCounter;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddToCartDtoResponse{");
        sb.append("CartCounter=").append(cartCounter);
        sb.append('}');
        return sb.toString();
    }
}
