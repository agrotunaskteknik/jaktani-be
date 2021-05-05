package com.cartas.jaktani.dto;

public class StatusWaybill {
    private float code;
    private String description;


    // Getter Methods

    public float getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    // Setter Methods

    public void setCode(float code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StatusWaybill{");
        sb.append("code=").append(code);
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
