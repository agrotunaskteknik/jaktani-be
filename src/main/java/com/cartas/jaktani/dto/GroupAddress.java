package com.cartas.jaktani.dto;

import java.util.List;

public class GroupAddress {
    private List<String> errors;
    private AddressDetailDto userAddress;
    private List<GroupShopDto> groupShop;

    public GroupAddress() {
    }

    public GroupAddress(List<String> errors, AddressDetailDto userAddress, List<GroupShopDto> groupShop) {
        this.errors = errors;
        this.userAddress = userAddress;
        this.groupShop = groupShop;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public AddressDetailDto getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(AddressDetailDto userAddress) {
        this.userAddress = userAddress;
    }

    public List<GroupShopDto> getGroupShop() {
        return groupShop;
    }

    public void setGroupShop(List<GroupShopDto> groupShop) {
        this.groupShop = groupShop;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GroupAddress{");
        sb.append("errors=").append(errors);
        sb.append(", userAddress=").append(userAddress);
        sb.append(", groupShop=").append(groupShop);
        sb.append('}');
        return sb.toString();
    }
}
