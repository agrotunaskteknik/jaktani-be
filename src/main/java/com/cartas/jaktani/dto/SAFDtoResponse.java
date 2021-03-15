package com.cartas.jaktani.dto;

import java.util.List;

public class SAFDtoResponse extends CommonResponse {
    private List<String> tickers;
    private List<GroupAddress> groupAddress;

    public SAFDtoResponse() {
    }

    public SAFDtoResponse(List<String> tickers, List<GroupAddress> groupAddress) {
        this.tickers = tickers;
        this.groupAddress = groupAddress;
    }

    public SAFDtoResponse(String errorMessage, String status, String message, List<String> tickers, List<GroupAddress> groupAddress) {
        super(errorMessage, status, message);
        this.tickers = tickers;
        this.groupAddress = groupAddress;
    }

    public List<String> getTickers() {
        return tickers;
    }

    public void setTickers(List<String> tickers) {
        this.tickers = tickers;
    }

    public List<GroupAddress> getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(List<GroupAddress> groupAddress) {
        this.groupAddress = groupAddress;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SAFDtoResponse{");
        sb.append("tickers=").append(tickers);
        sb.append(", groupAddress=").append(groupAddress);
        sb.append('}');
        return sb.toString();
    }
}
