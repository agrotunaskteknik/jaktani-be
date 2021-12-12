package com.cartas.jaktani.dto;

public class CallbackFVA {
    private String updated;
    private String created;
    private String payment_id;
    private String callback_virtual_account_id;
    private String owner_id;
    private String external_id;
    private String account_number;
    private String bank_code;
    private float amount;
    private String transaction_timestamp;
    private String merchant_code;
    private String id;
    private String status;

    public CallbackFVA() {
    }

    public CallbackFVA(String updated, String created, String payment_id, String callback_virtual_account_id, String owner_id, String external_id, String account_number, String bank_code, float amount, String transaction_timestamp, String merchant_code, String id, String status) {
        this.updated = updated;
        this.created = created;
        this.payment_id = payment_id;
        this.callback_virtual_account_id = callback_virtual_account_id;
        this.owner_id = owner_id;
        this.external_id = external_id;
        this.account_number = account_number;
        this.bank_code = bank_code;
        this.amount = amount;
        this.transaction_timestamp = transaction_timestamp;
        this.merchant_code = merchant_code;
        this.id = id;
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getPayment_id() {
        return payment_id;
    }

    public void setPayment_id(String payment_id) {
        this.payment_id = payment_id;
    }

    public String getCallback_virtual_account_id() {
        return callback_virtual_account_id;
    }

    public void setCallback_virtual_account_id(String callback_virtual_account_id) {
        this.callback_virtual_account_id = callback_virtual_account_id;
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

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getBank_code() {
        return bank_code;
    }

    public void setBank_code(String bank_code) {
        this.bank_code = bank_code;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getTransaction_timestamp() {
        return transaction_timestamp;
    }

    public void setTransaction_timestamp(String transaction_timestamp) {
        this.transaction_timestamp = transaction_timestamp;
    }

    public String getMerchant_code() {
        return merchant_code;
    }

    public void setMerchant_code(String merchant_code) {
        this.merchant_code = merchant_code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CallbackFVA{");
        sb.append("updated='").append(updated).append('\'');
        sb.append(", created='").append(created).append('\'');
        sb.append(", payment_id='").append(payment_id).append('\'');
        sb.append(", callback_virtual_account_id='").append(callback_virtual_account_id).append('\'');
        sb.append(", owner_id='").append(owner_id).append('\'');
        sb.append(", external_id='").append(external_id).append('\'');
        sb.append(", account_number='").append(account_number).append('\'');
        sb.append(", bank_code='").append(bank_code).append('\'');
        sb.append(", amount=").append(amount);
        sb.append(", transaction_timestamp='").append(transaction_timestamp).append('\'');
        sb.append(", merchant_code='").append(merchant_code).append('\'');
        sb.append(", id='").append(id).append('\'');
        sb.append(", status='").append(status).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
