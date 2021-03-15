package com.cartas.jaktani.dto;

public class VaNumber{
    private String bank;
    private String va_number;

    public VaNumber() {
    }

    public VaNumber(String bank, String va_number) {
        this.bank = bank;
        this.va_number = va_number;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getVa_number() {
        return va_number;
    }

    public void setVa_number(String va_number) {
        this.va_number = va_number;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VaNumber{");
        sb.append("bank='").append(bank).append('\'');
        sb.append(", va_number='").append(va_number).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
