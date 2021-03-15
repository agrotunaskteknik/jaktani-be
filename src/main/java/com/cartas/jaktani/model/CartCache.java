package com.cartas.jaktani.model;

import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

// timeToLive = 20 (20 is second)
@RedisHash(value = "CartCache",timeToLive = 20)
public class CartCache implements Serializable {
    private String id;
    private Long cartId;
    private Long userID;
    private Long shopID;
    private Long productID;
    private Long price;
    private Integer status;
    private Long quantity;
    private Long transactionID;
    private String notes;

    public CartCache(String id, Long cartId, Long userID, Long shopID, Long productID, Long price, Integer status, Long quantity, Long transactionID, String notes) {
        this.id = id;
        this.cartId = cartId;
        this.userID = userID;
        this.shopID = shopID;
        this.productID = productID;
        this.price = price;
        this.status = status;
        this.quantity = quantity;
        this.transactionID = transactionID;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Long getShopID() {
        return shopID;
    }

    public void setShopID(Long shopID) {
        this.shopID = shopID;
    }

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CartCache{");
        sb.append("id='").append(id).append('\'');
        sb.append(", cartId=").append(cartId);
        sb.append(", userID=").append(userID);
        sb.append(", shopID=").append(shopID);
        sb.append(", productID=").append(productID);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", quantity=").append(quantity);
        sb.append(", transactionID=").append(transactionID);
        sb.append(", notes='").append(notes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
