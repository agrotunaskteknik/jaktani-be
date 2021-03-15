package com.cartas.jaktani.dto;

public class CostResultDetailData {
    private Long value;
    private String etd;
    private String note;

    public CostResultDetailData(Long value, String etd, String note) {
        this.value = value;
        this.etd = etd;
        this.note = note;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getEtd() {
        return etd;
    }

    public void setEtd(String etd) {
        this.etd = etd;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CostResultDetailData{");
        sb.append("value=").append(value);
        sb.append(", etd='").append(etd).append('\'');
        sb.append(", note='").append(note).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
