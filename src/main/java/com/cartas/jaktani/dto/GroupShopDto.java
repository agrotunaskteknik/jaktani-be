package com.cartas.jaktani.dto;

import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.model.VwProductDetails;

import java.util.List;

public class GroupShopDto {
    private Shop shop;
    private List<String> errors;
    private String cartString;
    private Long shippingId;
    private Long spId;
    private VwProductDetails vwProductDto;

    public GroupShopDto() {
    }

    public GroupShopDto(Shop shop, List<String> errors, String cartString, Long shippingId, Long spId, VwProductDetails vwProductDto) {
        this.shop = shop;
        this.errors = errors;
        this.cartString = cartString;
        this.shippingId = shippingId;
        this.spId = spId;
        this.vwProductDto = vwProductDto;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public String getCartString() {
        return cartString;
    }

    public void setCartString(String cartString) {
        this.cartString = cartString;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public Long getSpId() {
        return spId;
    }

    public void setSpId(Long spId) {
        this.spId = spId;
    }

    public VwProductDetails getVwProductDto() {
        return vwProductDto;
    }

    public void setVwProductDto(VwProductDetails vwProductDto) {
        this.vwProductDto = vwProductDto;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupShopDto{");
        sb.append("shop=").append(shop);
        sb.append(", errors=").append(errors);
        sb.append(", cartString='").append(cartString).append('\'');
        sb.append(", shippingId=").append(shippingId);
        sb.append(", spId=").append(spId);
        sb.append(", vwProductDto=").append(vwProductDto);
        sb.append('}');
        return sb.toString();
    }
}