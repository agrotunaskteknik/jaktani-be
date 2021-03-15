package com.cartas.jaktani.dto;

public class CheckoutShopProduct {
    private Long cartId;
    private Long productId;
    private Long shopId;
    private Long spId;
    private Long shippingId;
    private String service;
    private String courier;
    private Long originCityId;
    private Long destincationCityId;

    public CheckoutShopProduct() {
    }

    public CheckoutShopProduct(Long cartId, Long productId, Long shopId, Long spId, Long shippingId, String service, String courier, Long originCityId, Long destincationCityId) {
        this.cartId = cartId;
        this.productId = productId;
        this.shopId = shopId;
        this.spId = spId;
        this.shippingId = shippingId;
        this.service = service;
        this.courier = courier;
        this.originCityId = originCityId;
        this.destincationCityId = destincationCityId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getSpId() {
        return spId;
    }

    public void setSpId(Long spId) {
        this.spId = spId;
    }

    public Long getShippingId() {
        return shippingId;
    }

    public void setShippingId(Long shippingId) {
        this.shippingId = shippingId;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public Long getOriginCityId() {
        return originCityId;
    }

    public void setOriginCityId(Long originCityId) {
        this.originCityId = originCityId;
    }

    public Long getDestincationCityId() {
        return destincationCityId;
    }

    public void setDestincationCityId(Long destincationCityId) {
        this.destincationCityId = destincationCityId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckoutShopProduct{");
        sb.append("cartId=").append(cartId);
        sb.append(", productId=").append(productId);
        sb.append(", shopId=").append(shopId);
        sb.append(", spId=").append(spId);
        sb.append(", shippingId=").append(shippingId);
        sb.append(", service='").append(service).append('\'');
        sb.append(", courier='").append(courier).append('\'');
        sb.append(", originCityId=").append(originCityId);
        sb.append(", destincationCityId=").append(destincationCityId);
        sb.append('}');
        return sb.toString();
    }
}
