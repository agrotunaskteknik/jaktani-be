package com.cartas.jaktani.dto;

import java.util.List;

public class CheckoutDtoRequest {
    private String addressId;
    private List<CheckoutShopProduct> shopProducts;
    private Long userId;

    public CheckoutDtoRequest() {
    }

    public CheckoutDtoRequest(String addressId, List<CheckoutShopProduct> shopProducts, Long userId) {
        this.addressId = addressId;
        this.shopProducts = shopProducts;
        this.userId = userId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public List<CheckoutShopProduct> getShopProducts() {
        return shopProducts;
    }

    public void setShopProducts(List<CheckoutShopProduct> shopProducts) {
        this.shopProducts = shopProducts;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckoutDtoRequest{");
        sb.append("addressId='").append(addressId).append('\'');
        sb.append(", shopProducts=").append(shopProducts);
        sb.append(", userId=").append(userId);
        sb.append('}');
        return sb.toString();
    }
}
