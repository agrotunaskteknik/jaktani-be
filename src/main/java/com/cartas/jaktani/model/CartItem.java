package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "shop_id")
    private Long shopID;

    @Column(name = "product_id")
    private Long productID;

    @Column(name = "price")
    private Long price;

    @Column(name = "status")
    private Integer status;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "transaction_id")
    private Long transactionID;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "updated_time")
    private Timestamp updatedTime;

    @Column(name = "resi_code")
    private String resiCode;

    @Column(name = "courier")
    private String courier;

    @Column(name = "service")
    private String service;

    @Column(name = "shop_address_id")
    private Long shopAddressID;

    @Column(name = "address_id")
    private Long addressID;

    public CartItem() {
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setShopID(Long shopId) {
        this.shopID = shopId;
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

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Timestamp getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Timestamp updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getResiCode() {
        return resiCode;
    }

    public void setResiCode(String resiCode) {
        this.resiCode = resiCode;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Long getShopAddressID() {
        return shopAddressID;
    }

    public void setShopAddressID(Long shopAddressID) {
        this.shopAddressID = shopAddressID;
    }

    public Long getAddressID() {
        return addressID;
    }

    public void setAddressID(Long addressID) {
        this.addressID = addressID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CartItem{");
        sb.append("id=").append(id);
        sb.append(", userID=").append(userID);
        sb.append(", shopID=").append(shopID);
        sb.append(", productID=").append(productID);
        sb.append(", price=").append(price);
        sb.append(", status=").append(status);
        sb.append(", quantity=").append(quantity);
        sb.append(", transactionID=").append(transactionID);
        sb.append(", notes='").append(notes).append('\'');
        sb.append(", createdTime=").append(createdTime);
        sb.append(", updatedTime=").append(updatedTime);
        sb.append(", resiCode='").append(resiCode).append('\'');
        sb.append(", courier='").append(courier).append('\'');
        sb.append(", service='").append(service).append('\'');
        sb.append(", shopAddressID=").append(shopAddressID);
        sb.append(", addressID=").append(addressID);
        sb.append('}');
        return sb.toString();
    }
}
