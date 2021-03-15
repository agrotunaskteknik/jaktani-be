package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.model.Address;
import com.cartas.jaktani.repository.*;
import com.cartas.jaktani.util.Utils;
import com.google.gson.Gson;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class AddressServiceImpl implements AddressService {
    Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);

    public final static Integer TYPE_USER = 1;
    public final static Integer TYPE_SHOP = 2;
    public final static Integer STATUS_DEFAULT = 1;
    public final static Integer STATUS_DELETED = 0;
    public final static Integer STATUS_ACTIVE = 2;
    // one instance, reuse
    private final OkHttpClient httpClient = new OkHttpClient();

    private static final String serviceURL = "https://api.rajaongkir.com/starter/";
    Gson gson = new Gson();


    @Autowired
    AddressRepository addressRepository;

    @Override
    public AddressDetailDto getUserDefaultAddress(Integer userId) {
        Optional<Address> addressOptional = addressRepository.findByTypeAndRelationIdAndStatusIs(TYPE_USER, userId, STATUS_DEFAULT);
        if (!addressOptional.isPresent()) {
            logger.debug("Empty default user Address");
            return new AddressDetailDto();
        }
        Address address = addressOptional.get();
        AddressDetailDto addressDetailDto = new AddressDetailDto();
        addressDetailDto.setAddressId(address.getId().toString());
        addressDetailDto.setCityId(address.getCity());
        addressDetailDto.setPostalCode(address.getPostalCode());
        addressDetailDto.setProvinceId(address.getProvince());
        addressDetailDto.setRelationId(address.getRelationId());
        addressDetailDto.setStatus(address.getStatus());
        addressDetailDto.setDescription(address.getDescription());
        addressDetailDto.setType(address.getType());
        try {
            CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
            addressDetailDto.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
            addressDetailDto.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
        } catch (Exception ex) {
            logger.debug("error get detail ex: " + ex.getMessage());
        }

        return addressDetailDto;
    }

    @Override
    public AddressDetailDto getByAddressId(Integer addressId) {
        Optional<Address> addressOptional = addressRepository.findByIdAndStatusIsNot(addressId, STATUS_DELETED);
        if (!addressOptional.isPresent()) {
            logger.debug("address = " + addressId + " not found");
            return new AddressDetailDto();
        }
        Address address = addressOptional.get();
        AddressDetailDto addressDetailDto = new AddressDetailDto();
        addressDetailDto.setAddressId(address.getId().toString());
        addressDetailDto.setCityId(address.getCity());
        addressDetailDto.setPostalCode(address.getPostalCode());
        addressDetailDto.setProvinceId(address.getProvince());
        addressDetailDto.setRelationId(address.getRelationId());
        addressDetailDto.setStatus(address.getStatus());
        addressDetailDto.setDescription(address.getDescription());
        addressDetailDto.setType(address.getType());
        try {
            CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
            addressDetailDto.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
            addressDetailDto.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
        } catch (Exception ex) {
            logger.debug("error get detail ex: " + ex.getMessage());
        }
        return addressDetailDto;
    }

    @Override
    public AddressDetailDto saveAddress(AddressDetailDto addressDetailDto) {
        if (addressDetailDto.getStatus().equals(STATUS_DELETED)) {
            logger.debug("address status deleted");
            return new AddressDetailDto();
        }
        if (addressDetailDto.getType().equals(0)) {
            logger.debug("type is empty");
            return new AddressDetailDto();
        }
        if (addressDetailDto.getRelationId().equals(0)) {
            logger.debug("relation id is empty");
            return new AddressDetailDto();
        }
        if (addressDetailDto.getCityId().trim().equalsIgnoreCase("")) {
            logger.debug("city id is empty");
            return new AddressDetailDto();
        }
        if (addressDetailDto.getProvinceId().trim().equalsIgnoreCase("")) {
            logger.debug("province id is empty");
            return new AddressDetailDto();
        }
        Address address = new Address();
        address.setPostalCode(addressDetailDto.getPostalCode());
        address.setProvince(addressDetailDto.getPostalCode());
        address.setCity(addressDetailDto.getCityId());
        address.setDescription(addressDetailDto.getDescription());
        address.setType(addressDetailDto.getType());
        address.setRelationId(addressDetailDto.getRelationId());
        address.setStatus(addressDetailDto.getStatus());
        address.setCreatedBy(addressDetailDto.getRelationId());
        address.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        address = addressRepository.save(address);
        AddressDetailDto addressDetailDtoSaved = new AddressDetailDto();
        addressDetailDtoSaved.setAddressId(address.getId().toString());
        addressDetailDtoSaved.setCityId(address.getCity());
        addressDetailDtoSaved.setPostalCode(address.getPostalCode());
        addressDetailDtoSaved.setProvinceId(address.getProvince());
        addressDetailDtoSaved.setRelationId(address.getRelationId());
        addressDetailDtoSaved.setStatus(address.getStatus());
        addressDetailDtoSaved.setType(address.getType());
        try {
            CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
            addressDetailDtoSaved.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
            addressDetailDtoSaved.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
        } catch (Exception ex) {
            logger.debug("error get detail ex: " + ex.getMessage());
        }
        return addressDetailDtoSaved;
    }

    @Override
    public AddressDetailDto updateAddress(AddressDetailDto addressDetailDto) {
        Integer addressId = Integer.valueOf(addressDetailDto.getAddressId());
        Optional<Address> addressOptional = addressRepository.findByIdAndStatusIsNot(addressId, STATUS_DELETED);
        if (!addressOptional.isPresent()) {
            logger.debug("address = " + addressId + " not found");
            return new AddressDetailDto();
        }
        Address address = addressOptional.get();
        if (addressDetailDto.getStatus().equals(STATUS_DELETED)) {
            logger.debug("address already deleted");
            return new AddressDetailDto();
        }
        if (!addressDetailDto.getType().equals(address.getType())) {
            logger.debug("type is not the same");
            return new AddressDetailDto();
        }
        if (!addressDetailDto.getRelationId().equals(address.getRelationId())) {
            logger.debug("relation id is not the same");
            return new AddressDetailDto();
        }
        address.setCity(addressDetailDto.getCityId());
        address.setDescription(addressDetailDto.getDescription());
        address.setProvince(addressDetailDto.getProvinceId());
        address.setPostalCode(addressDetailDto.getPostalCode());
        address.setUpdatedBy(addressDetailDto.getRelationId());
        address.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        address = addressRepository.save(address);
        AddressDetailDto addressDetailDtoUpdated = new AddressDetailDto();
        addressDetailDtoUpdated.setAddressId(address.getId().toString());
        addressDetailDtoUpdated.setCityId(address.getCity());
        addressDetailDtoUpdated.setPostalCode(address.getPostalCode());
        addressDetailDtoUpdated.setProvinceId(address.getProvince());
        addressDetailDtoUpdated.setRelationId(address.getRelationId());
        addressDetailDtoUpdated.setStatus(address.getStatus());
        addressDetailDtoUpdated.setDescription(address.getDescription());
        addressDetailDtoUpdated.setType(address.getType());
        try {
            CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
            addressDetailDtoUpdated.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
            addressDetailDtoUpdated.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
        } catch (Exception ex) {
            logger.debug("error get detail ex: " + ex.getMessage());
        }
        return addressDetailDtoUpdated;
    }

    @Override
    public AddressDetailDto deleteAddress(AddressDetailDto addressDetailDto) {
        addressDetailDto.setStatus(STATUS_DELETED);
        addressDetailDto = updateAddress(addressDetailDto);
        return addressDetailDto;
    }

    @Override
    public AddressDetailDto getShopDefaultAddress(Integer shopId) {
        Optional<Address> addressOptional = addressRepository.findByTypeAndRelationIdAndStatusIs(TYPE_SHOP, shopId, STATUS_DEFAULT);
        if (!addressOptional.isPresent()) {
            logger.debug("Empty shop default Address");
            return new AddressDetailDto();
        }
        Address address = addressOptional.get();
        AddressDetailDto addressDetailDto = new AddressDetailDto();
        addressDetailDto.setAddressId(address.getId().toString());
        addressDetailDto.setCityId(address.getCity());
        addressDetailDto.setPostalCode(address.getPostalCode());
        addressDetailDto.setProvinceId(address.getProvince());
        addressDetailDto.setRelationId(address.getRelationId());
        addressDetailDto.setStatus(address.getStatus());
        addressDetailDto.setDescription(address.getDescription());
        addressDetailDto.setType(address.getType());
        try {
            CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
            addressDetailDto.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
            addressDetailDto.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
        } catch (Exception ex) {
            logger.debug("error get detail ex: " + ex.getMessage());
        }
        return addressDetailDto;
    }

    @Override
    public List<AddressDetailDto> getAllUserAddresses(Integer userId) {
        List<Address> addresses = addressRepository.findAllByTypeAndRelationIdAndStatusIsNot(TYPE_USER, userId, STATUS_DELETED);
        List<AddressDetailDto> addressDetailDtos = new ArrayList<>();
        if (addresses == null || addresses.size() == 0) {
            logger.debug("Empty user Address");
            return addressDetailDtos;
        }
        for (Address address : addresses) {
            AddressDetailDto addressDetailDto = new AddressDetailDto();
            addressDetailDto.setAddressId(address.getId().toString());
            addressDetailDto.setCityId(address.getCity());
            addressDetailDto.setPostalCode(address.getPostalCode());
            addressDetailDto.setProvinceId(address.getProvince());
            addressDetailDto.setRelationId(address.getRelationId());
            addressDetailDto.setStatus(address.getStatus());
            addressDetailDto.setDescription(address.getDescription());
            addressDetailDto.setType(address.getType());
            try {
                CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
                addressDetailDto.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
                addressDetailDto.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
            } catch (Exception ex) {
                logger.debug("error get detail ex: " + ex.getMessage());
            }
            addressDetailDtos.add(addressDetailDto);
        }
        return addressDetailDtos;
    }

    @Override
    public List<AddressDetailDto> getAllShopAddresses(Integer shopId) {
        List<Address> addresses = addressRepository.findAllByTypeAndRelationIdAndStatusIsNot(TYPE_SHOP, shopId, STATUS_DELETED);
        List<AddressDetailDto> addressDetailDtos = new ArrayList<>();

        if (addresses == null || addresses.size() == 0) {
            logger.debug("Empty shop Address");
            return new ArrayList<>();
        }
        for (Address address : addresses) {
            AddressDetailDto addressDetailDto = new AddressDetailDto();
            addressDetailDto.setAddressId(address.getId().toString());
            addressDetailDto.setCityId(address.getCity());
            addressDetailDto.setPostalCode(address.getPostalCode());
            addressDetailDto.setProvinceId(address.getProvince());
            addressDetailDto.setRelationId(address.getRelationId());
            addressDetailDto.setStatus(address.getStatus());
            addressDetailDto.setDescription(address.getDescription());
            addressDetailDto.setType(address.getType());
            try {
                CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(address.getProvince(), address.getCity());
                addressDetailDto.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
                addressDetailDto.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
            } catch (Exception ex) {
                logger.debug("error get detail ex: " + ex.getMessage());
            }
            addressDetailDtos.add(addressDetailDto);
        }
        return addressDetailDtos;
    }

    @Override
    public ProvinceParent getProvinces() throws IOException {
        Request request = new Request.Builder()
                .url(serviceURL + "province")
                .addHeader("key", "68d3e4ddba14b33ee44d8bc9656f444f")  // add request headers
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            ProvinceParent entity = gson.fromJson(jsonString, ProvinceParent.class);
            return entity;
        }

    }

    @Override
    public CityParent getCitiesByProvinceId(String provinceId) throws IOException {
        String query = "city";
        if (provinceId != null && !provinceId.trim().equalsIgnoreCase("")) {
            query += "?province=" + provinceId;
        }
        Request request = new Request.Builder()
                .url(serviceURL + query)
                .addHeader("key", "68d3e4ddba14b33ee44d8bc9656f444f")  // add request headers
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            CityParent entity = gson.fromJson(jsonString, CityParent.class);
            return entity;
        }

    }

    public CityParentSingle getCitiesByProvinceIdAndCityId(String provinceId, String cityId) throws IOException {
        String query = "city";
        if (provinceId != null && !provinceId.trim().equalsIgnoreCase("")) {
            query += "?province=" + provinceId + "id=" + cityId;
        }
        Request request = new Request.Builder()
                .url(serviceURL + query)
                .addHeader("key", "68d3e4ddba14b33ee44d8bc9656f444f")  // add request headers
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            CityParentSingle entity = gson.fromJson(jsonString, CityParentSingle.class);
            return entity;
        }

    }

    @Override
    public CostParent getCostByCityId(String originCityId, String destinationCityId, Long weight, String courier) throws IOException {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "origin=" + originCityId + "&destination=" + destinationCityId +
                "&weight=" + weight + "&courier=" + courier);
        Request request = new Request.Builder()
                .url("https://api.rajaongkir.com/starter/cost")
                .post(body)
                .addHeader("key", "68d3e4ddba14b33ee44d8bc9656f444f")  // add request headers
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            CostParent entity = gson.fromJson(jsonString, CostParent.class);
            return entity;
        }

    }
}