package com.cartas.jaktani.dto;

public class CallbackVAXendit {
    private boolean is_closed;
    private String status;
    private String owner_id;
    private String external_id;
    private String bank_code;
    private String merchant_code;
    private String name;
    private String account_number;
    private Float expected_amount;
    private boolean is_single_use;
    private String expiration_date;
    private String id;

    public CallbackVAXendit() {
    }

    public CallbackVAXendit(boolean is_closed, String status, String owner_id, String external_id, String bank_code, String merchant_code, String name, String account_number, float expected_amount, boolean is_single_use, String expiration_date, String id) {
        this.is_closed = is_closed;
        this.status = status;
        this.owner_id = owner_id;
        this.external_id = external_id;
        this.bank_code = bank_code;
        this.merchant_code = merchant_code;
        this.name = name;
        this.account_number = account_number;
        this.expected_amount = expected_amount;
        this.is_single_use = is_single_use;
        this.expiration_date = expiration_date;
        this.id = id;
    }

    public boolean isIs_closed() {
        return is_closed;
    }

    public void setIs_closed(boolean is_closed) {
        this.is_closed = is_closed;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getExternal_id() {
        return external_id;
    }

    public void setExternal_id(String external_id) {
        this.external_id = external_id;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public Float getExpected_amount() {
        return expected_amount;
    }

    public void setExpected_amount(Float expected_amount) {
        this.expected_amount = expected_amount;
    }

    public boolean isIs_single_use() {
        return is_single_use;
    }

    public void setIs_single_use(boolean is_single_use) {
        this.is_single_use = is_single_use;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallbackVAXendit{");
        sb.append("is_closed=").append(is_closed);
        sb.append(", status='").append(status).append('\'');
        sb.append(", owner_id='").append(owner_id).append('\'');
        sb.append(", external_id='").append(external_id).append('\'');
        sb.append(", bank_code='").append(bank_code).append('\'');
        sb.append(", merchant_code='").append(merchant_code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", account_number='").append(account_number).append('\'');
        sb.append(", expected_amount=").append(expected_amount);
        sb.append(", is_single_use=").append(is_single_use);
        sb.append(", expiration_date='").append(expiration_date).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
