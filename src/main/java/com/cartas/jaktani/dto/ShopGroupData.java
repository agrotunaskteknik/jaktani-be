package com.cartas.jaktani.dto;

import com.cartas.jaktani.model.Shop;

import java.util.List;

public class ShopGroupData {
    private String tickerMessage;
    private Integer status;
    private Shop shop;
    private List<CartDetails> cartDetails;

    public ShopGroupData() {
    }

    public ShopGroupData(String tickerMessage, Integer status, Shop shop, List<CartDetails> cartDetails) {
        this.tickerMessage = tickerMessage;
        this.status = status;
        this.shop = shop;
        this.cartDetails = cartDetails;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTickerMessage() {
        return tickerMessage;
    }

    public void setTickerMessage(String tickerMessage) {
        this.tickerMessage = tickerMessage;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<CartDetails> getCartDetails() {
        return cartDetails;
    }

    public void setCartDetails(List<CartDetails> cartDetails) {
        this.cartDetails = cartDetails;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ShopGroupData{");
        sb.append("tickerMessage='").append(tickerMessage).append('\'');
        sb.append(", status=").append(status);
        sb.append(", shop=").append(shop);
        sb.append(", cartDetails=").append(cartDetails);
        sb.append('}');
        return sb.toString();
    }
}
