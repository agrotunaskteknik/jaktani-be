package com.cartas.jaktani.dto;

import java.util.List;

public class CartListResponse extends CommonResponse {
    private List<ShopGroupData> shopGroupAvailable;
    private List<ShopGroupData> shopGroupUnavailable;

    public CartListResponse() {
    }

    public CartListResponse(List<ShopGroupData> shopGroupAvailable, List<ShopGroupData> shopGroupUnavailable) {
        this.shopGroupAvailable = shopGroupAvailable;
        this.shopGroupUnavailable = shopGroupUnavailable;
    }

    public CartListResponse(String errorMessage, String status, String message, List<ShopGroupData> shopGroupAvailable, List<ShopGroupData> shopGroupUnavailable) {
        super(errorMessage, status, message);
        this.shopGroupAvailable = shopGroupAvailable;
        this.shopGroupUnavailable = shopGroupUnavailable;
    }

    public List<ShopGroupData> getShopGroupAvailable() {
        return shopGroupAvailable;
    }

    public void setShopGroupAvailable(List<ShopGroupData> shopGroupAvailable) {
        this.shopGroupAvailable = shopGroupAvailable;
    }

    public List<ShopGroupData> getShopGroupUnavailable() {
        return shopGroupUnavailable;
    }

    public void setShopGroupUnavailable(List<ShopGroupData> shopGroupUnavailable) {
        this.shopGroupUnavailable = shopGroupUnavailable;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CartListResponse{");
        sb.append(", shopGroupAvailable=").append(shopGroupAvailable);
        sb.append(", shopGroupUnavailable=").append(shopGroupUnavailable);
        sb.append('}');
        return sb.toString();
    }
}
