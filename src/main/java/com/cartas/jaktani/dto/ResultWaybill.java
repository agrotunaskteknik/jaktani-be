package com.cartas.jaktani.dto;

import java.util.ArrayList;

public class ResultWaybill {
    private boolean delivered;
    SummaryWaybill summary;
    DetailsWaybill details;
    ArrayList<ManifestWaybill> manifest = new ArrayList<>();
    DeliveryStatusWaybill delivery_status;


    // Getter Methods

    public boolean getDelivered() {
        return delivered;
    }

    public SummaryWaybill getSummary() {
        return summary;
    }

    public DetailsWaybill getDetails() {
        return details;
    }

    public DeliveryStatusWaybill getDelivery_status() {
        return delivery_status;
    }

    // Setter Methods

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public void setSummary(SummaryWaybill summaryObject) {
        this.summary = summaryObject;
    }

    public void setDetails(DetailsWaybill detailsObject) {
        this.details = detailsObject;
    }

    public void setDelivery_status(DeliveryStatusWaybill delivery_statusObject) {
        this.delivery_status = delivery_statusObject;
    }

    public boolean isDelivered() {
        return delivered;
    }

    public ArrayList<ManifestWaybill> getManifest() {
        return manifest;
    }

    public void setManifest(ArrayList<ManifestWaybill> manifest) {
        this.manifest = manifest;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ResultWaybill{");
        sb.append("delivered=").append(delivered);
        sb.append(", SummaryObject=").append(summary);
        sb.append(", DetailsObject=").append(details);
        sb.append(", manifest=").append(manifest);
        sb.append(", deliveryStatusWaybill=").append(delivery_status);
        sb.append('}');
        return sb.toString();
    }
}
