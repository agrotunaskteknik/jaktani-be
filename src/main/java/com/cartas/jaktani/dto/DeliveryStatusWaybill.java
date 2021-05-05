package com.cartas.jaktani.dto;

public class DeliveryStatusWaybill {
    private String status;
    private String pod_receiver;
    private String pod_date;
    private String pod_time;


    // Getter Methods

    public String getStatus() {
        return status;
    }

    public String getPod_receiver() {
        return pod_receiver;
    }

    public String getPod_date() {
        return pod_date;
    }

    public String getPod_time() {
        return pod_time;
    }

    // Setter Methods

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPod_receiver(String pod_receiver) {
        this.pod_receiver = pod_receiver;
    }

    public void setPod_date(String pod_date) {
        this.pod_date = pod_date;
    }

    public void setPod_time(String pod_time) {
        this.pod_time = pod_time;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DeliveryStatusWaybill{");
        sb.append("status='").append(status).append('\'');
        sb.append(", pod_receiver='").append(pod_receiver).append('\'');
        sb.append(", pod_date='").append(pod_date).append('\'');
        sb.append(", pod_time='").append(pod_time).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
