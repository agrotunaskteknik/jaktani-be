package com.cartas.jaktani.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "order_transaction")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "gross_amount")
    private Long grossAmount;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "transaction_time")
    private Timestamp transactionTime;

    @Column(name = "transaction_id")
    private String transactionID;

    @Column(name = "va_number")
    private String vaNumber;

    @Column(name = "metadata")
    private String metadata;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "shop_id")
    private Long shopId;

    @Column(name = "status")
    private Integer status;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "resi_code")
    private String resiCode;

    @Column(name = "customer_address")
    private Integer custAddress;

    @Column(name = "shop_address")
    private Integer shopAddress;

    @Column(name = "courier")
    private String courier;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "service")
    private String service;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long userID) {
        this.customerId = userID;
    }

    public Long getGrossAmount() {
        return grossAmount;
    }

    public void setGrossAmount(Long grossAmount) {
        this.grossAmount = grossAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public Timestamp getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Timestamp transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

    public String getVaNumber() {
        return vaNumber;
    }

    public void setVaNumber(String vaNumber) {
        this.vaNumber = vaNumber;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getResiCode() {
        return resiCode;
    }

    public void setResiCode(String resiCode) {
        this.resiCode = resiCode;
    }

    public Integer getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(Integer custAddress) {
        this.custAddress = custAddress;
    }

    public Integer getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(Integer shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getCourier() {
        return courier;
    }

    public void setCourier(String courier) {
        this.courier = courier;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append("id=").append(id);
        sb.append(", customerId=").append(customerId);
        sb.append(", grossAmount=").append(grossAmount);
        sb.append(", paymentType='").append(paymentType).append('\'');
        sb.append(", transactionStatus='").append(transactionStatus).append('\'');
        sb.append(", transactionTime=").append(transactionTime);
        sb.append(", transactionID='").append(transactionID).append('\'');
        sb.append(", vaNumber='").append(vaNumber).append('\'');
        sb.append(", metadata='").append(metadata).append('\'');
        sb.append(", createdDate=").append(createdDate);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", shopId=").append(shopId);
        sb.append(", status=").append(status);
        sb.append(", quantity=").append(quantity);
        sb.append(", resiCode='").append(resiCode).append('\'');
        sb.append(", custAddress=").append(custAddress);
        sb.append(", shopAddress=").append(shopAddress);
        sb.append(", courier='").append(courier).append('\'');
        sb.append(", weight=").append(weight);
        sb.append(", service='").append(service).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
