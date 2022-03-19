package com.cartas.jaktani.dto;

public class VerifyOrderShipping {
    private String noResi;
    private Long orderID;

    public VerifyOrderShipping() {
    }

    public VerifyOrderShipping(String noResi, Long orderID) {
        this.noResi = noResi;
        this.orderID = orderID;
    }

    public String getNoResi() {
        return noResi;
    }

    public void setNoResi(String noResi) {
        this.noResi = noResi;
    }

    public Long getOrderID() {
        return orderID;
    }

    public void setOrderID(Long orderID) {
        this.orderID = orderID;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VerifyOrderShipping{");
        sb.append("NoResi='").append(noResi).append('\'');
        sb.append(", OrderID=").append(orderID);
        sb.append('}');
        return sb.toString();
    }
}


