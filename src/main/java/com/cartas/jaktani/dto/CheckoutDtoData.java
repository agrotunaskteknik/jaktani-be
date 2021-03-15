package com.cartas.jaktani.dto;

import java.util.List;

public class CheckoutDtoData {
    private CheckoutParameterResponse parameter;
    private List<CheckoutProductData> productList;

    public CheckoutDtoData() {
    }

    public CheckoutDtoData(CheckoutParameterResponse parameter, List<CheckoutProductData> productList) {
        this.parameter = parameter;
        this.productList = productList;
    }

    public CheckoutParameterResponse getParameter() {
        return parameter;
    }

    public void setParameter(CheckoutParameterResponse parameter) {
        this.parameter = parameter;
    }

    public List<CheckoutProductData> getProductList() {
        return productList;
    }

    public void setProductList(List<CheckoutProductData> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CheckoutDtoData{");
        sb.append("parameter=").append(parameter);
        sb.append(", productList=").append(productList);
        sb.append('}');
        return sb.toString();
    }
}
