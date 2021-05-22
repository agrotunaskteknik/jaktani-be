package com.cartas.jaktani.dto;

import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.model.VwProductDetails;

import java.util.List;

public class OrderDetailDto {
    private VwProductDetails product;
    private Shop shop;
    private String orderStatusTitle;
    private Integer orderStatus;
    private String iconImg;
    private String orderTransactionDateString;
    private Long orderTotal;
    private Long orderTotalAmount;
    private Long orderID;
    private RajaOngkirWaybillResponseDto detailWaybill;
    private UserDto userDto;

    public OrderDetailDto() {
    }

    public OrderDetailDto(VwProductDetails product, Shop shop, String orderStatusTitle, Integer orderStatus, String iconImg, String orderTransactionDateString, Long orderTotal, Long orderTotalAmount, Long orderID, RajaOngkirWaybillResponseDto detailWaybill, UserDto userDto) {
        this.product = product;
        this.shop = shop;
        this.orderStatusTitle = orderStatusTitle;
        this.orderStatus = orderStatus;
        this.iconImg = iconImg;
        this.orderTransactionDateString = orderTransactionDateString;
        this.orderTotal = orderTotal;
        this.orderTotalAmount = orderTotalAmount;
        this.orderID = orderID;
        this.detailWaybill = detailWaybill;
        this.userDto = userDto;
    }

    public VwProductDetails getProduct() {
        return product;
    }

    public void setProduct(VwProductDetails product) {
        this.product = product;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getOrderStatusTitle() {
        return orderStatusTitle;
    }

    public void setOrderStatusTitle(String orderStatusTitle) {
        this.orderStatusTitle = orderStatusTitle;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getIconImg() {
        return iconImg;
    }

    public void setIconImg(String iconImg) {
        this.iconImg = iconImg;
    }

    public String getOrderTransactionDateString() {
        return orderTransactionDateString;
    }

    public void setOrderTransactionDateString(String orderTransactionDateString) {
        this.orderTransactionDateString = orderTransactionDateString;
    }

    public Long getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Long orderTotal) {
        this.orderTotal = orderTotal;
    }

    public Long getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(Long orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    public RajaOngkirWaybillResponseDto getDetailWaybill() {
        return detailWaybill;
    }

    public void setDetailWaybill(RajaOngkirWaybillResponseDto detailWaybill) {
        this.detailWaybill = detailWaybill;
    }

    public UserDto getUserDto() {
        return userDto;
    }

    public void setUserDto(UserDto userDto) {
        this.userDto = userDto;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("OrderDetailDto{");
        sb.append("product=").append(product);
        sb.append(", shop=").append(shop);
        sb.append(", orderStatusTitle='").append(orderStatusTitle).append('\'');
        sb.append(", orderStatus=").append(orderStatus);
        sb.append(", iconImg='").append(iconImg).append('\'');
        sb.append(", orderTransactionDateString='").append(orderTransactionDateString).append('\'');
        sb.append(", orderTotal=").append(orderTotal);
        sb.append(", orderTotalAmount=").append(orderTotalAmount);
        sb.append(", orderID=").append(orderID);
        sb.append(", detailWaybill=").append(detailWaybill);
        sb.append(", userDto=").append(userDto);
        sb.append('}');
        return sb.toString();
    }
}
