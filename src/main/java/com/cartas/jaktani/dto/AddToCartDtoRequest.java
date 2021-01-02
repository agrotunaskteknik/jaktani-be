package com.cartas.jaktani.dto;

public class AddToCartDtoRequest {
    private Long cartID;
    private Long userID;
    private Long productID;
    private Long shopID;
    private Long quantity;
    private String notes;

    public AddToCartDtoRequest() {
    }

    public AddToCartDtoRequest(Long cartID, Long userID, Long productID, Long shopID, Long quantity, String notes) {
        this.cartID = cartID;
        this.userID = userID;
        this.productID = productID;
        this.shopID = shopID;
        this.quantity = quantity;
        this.notes = notes;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getCartID() {
        return cartID;
    }

    public void setCartID(Long cartID) {
        this.cartID = cartID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Long getShopID() {
        return shopID;
    }

    public void setShopID(Long shopID) {
        this.shopID = shopID;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AddToCartDto{");
        sb.append("cartID=").append(cartID);
        sb.append(", productID=").append(productID);
        sb.append(", userID=").append(userID);
        sb.append(", shopID=").append(shopID);
        sb.append(", quantity=").append(quantity);
        sb.append(", notes='").append(notes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
