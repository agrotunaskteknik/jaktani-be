package com.cartas.jaktani.dto;

public class VaNumberDto {
    private String bank;
    private String vaNumber;
    private String billKey;
    private String billerCode;

    public VaNumberDto() {
    }

    public VaNumberDto(String bank, String vaNumber, String billKey, String billerCode) {
        this.bank = bank;
        this.vaNumber = vaNumber;
        this.billKey = billKey;
        this.billerCode = billerCode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getVaNumber() {
        return vaNumber;
    }

    public void setVaNumber(String vaNumber) {
        this.vaNumber = vaNumber;
    }

    public String getBillKey() {
        return billKey;
    }

    public void setBillKey(String billKey) {
        this.billKey = billKey;
    }

    public String getBillerCode() {
        return billerCode;
    }

    public void setBillerCode(String billerCode) {
        this.billerCode = billerCode;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("VaNumberDto{");
        sb.append("bank='").append(bank).append('\'');
        sb.append(", vaNumber='").append(vaNumber).append('\'');
        sb.append(", billKey='").append(billKey).append('\'');
        sb.append(", billerCode='").append(billerCode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
