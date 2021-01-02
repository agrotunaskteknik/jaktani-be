package com.cartas.jaktani.dto;

import com.cartas.jaktani.model.VwProductDetails;

public class CartDetails {
    private Long id;
    private Long quantity;
    private Long price;
    private String notes;
    private VwProductDetails productDto;

    public CartDetails() {
    }

    public CartDetails(Long id, Long quantity, Long price, String notes, VwProductDetails productDto) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.notes = notes;
        this.productDto = productDto;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public VwProductDetails getVWProductDto() {
        return productDto;
    }

    public void setVWProductDto(VwProductDetails productDto) {
        this.productDto = productDto;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CartDetails{");
        sb.append("id=").append(id);
        sb.append(", tickerMessage='").append(notes).append('\'');
        sb.append(", productDto=").append(productDto);
        sb.append('}');
        return sb.toString();
    }
}
