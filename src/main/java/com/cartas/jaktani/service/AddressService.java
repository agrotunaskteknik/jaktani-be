package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.Address;

import java.io.IOException;
import java.util.List;

public interface AddressService {
    AddressDetailDto getUserDefaultAddress(Integer userId);

    AddressDetailDto getByAddressId(Integer addressId);

    AddressDetailDto saveAddress(AddressDetailDto addressDetailDto);

    AddressDetailDto updateAddress(AddressDetailDto addressDetailDto);

    AddressDetailDto deleteAddress(AddressDetailDto addressDetailDto);

    AddressDetailDto getShopDefaultAddress(Integer shopId);

    List<AddressDetailDto> getAllUserAddresses(Integer userId);

    List<AddressDetailDto> getAllShopAddresses(Integer shopId);

    ProvinceParent getProvinces() throws IOException;

    CityParent getCitiesByProvinceId(String provinceId) throws IOException;

    CostParent getCostByCityId(String originCityId, String destinationCityId, Long weight, String courier) throws IOException;

    List<AddressDetailDto> setDefaultAddress(AddressDetailDto addressDetailDto);

    AddressDetailDto getDefaultAddressByIdAndRelationType(Integer id, Integer relationType);
}
