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
    public final static String apiKeyOld = "68d3e4ddba14b33ee44d8bc9656f444f";
    public final static String apiKeyNew = "2b2425cce3083503dbb981c9bd682b5e";
    // one instance, reuse
    private final OkHttpClient httpClient = new OkHttpClient();

    private static final String serviceURL = "https://pro.rajaongkir.com/api/";
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
    public AddressDetailDto getDefaultAddressByIdAndRelationType(Integer id, Integer relationType) {
        Optional<Address> addressOptional = addressRepository.findByTypeAndRelationIdAndStatusIs(relationType, id, STATUS_DEFAULT);
        if (!addressOptional.isPresent()) {
            logger.debug("address for id = " + id + " not found");
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
        address.setProvince(addressDetailDto.getProvinceId());
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
                .addHeader("key", apiKeyNew)  // add request headers
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
                .addHeader("key", apiKeyNew)  // add request headers
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
            query += "?province=" + provinceId + "&id=" + cityId;
        }
        Request request = new Request.Builder()
                .url(serviceURL + query)
                .addHeader("key", apiKeyNew)  // add request headers
                .build();
        CityParentSingle entity = new CityParentSingle();
        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            try {
                CityParent entity2 = gson.fromJson(jsonString, CityParent.class);
                if (entity2 != null && entity2.getRajaongkir() != null && entity2.getRajaongkir().getResults().size() > 0) {
                    RajaOngkirCity cityMulti = entity2.getRajaongkir();
                    RajaOngkirCitySingle citySingle = new RajaOngkirCitySingle(cityMulti.getQuery(), cityMulti.getResults().get(0), cityMulti.getStatus());
                    entity.setRajaongkir(citySingle);
                }
                return entity;

            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                CityParentSingle entity2 = gson.fromJson(jsonString, CityParentSingle.class);
                if (entity2 != null && entity2.getRajaongkir() != null) {
                    RajaOngkirCitySingle citySingle = entity2.getRajaongkir();
                    entity.setRajaongkir(citySingle);
                }
                return entity;
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return entity;
    }

    @Override
    public CostParent getCostByCityId(String originCityId, String destinationCityId, Long weight, String courier) throws IOException {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "origin=" + originCityId + "&destination=" + destinationCityId +
                "&weight=" + weight + "&courier=" + courier+"&originType=city&destinationType=city");
        Request request = new Request.Builder()
                .url("https://pro.rajaongkir.com/api/cost")
                .post(body)
                .addHeader("key", apiKeyNew)  // add request headers
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

    @Override
    public RajaOngkirWaybillResponseDto getWaybillDetail(String waybill, String courier) throws IOException {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "waybill=" + waybill + "&courier=" + courier);
        Request request = new Request.Builder()
                .url("https://pro.rajaongkir.com/api/waybill")
                .post(body)
                .addHeader("key", apiKeyNew)  // add request headers
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            // Get response body
            String jsonString = Objects.requireNonNull(response.body()).string();
            System.out.println(jsonString);
            RajaOngkirWaybillResponseDto entity = gson.fromJson(jsonString, RajaOngkirWaybillResponseDto.class);
            return entity;
        }

    }

    public List<AddressDetailDto> setDefaultAddress(AddressDetailDto addressDetailDto) {
        List<AddressDetailDto> addressDetailDtos = new ArrayList<>();
        Integer addressId = Integer.valueOf(addressDetailDto.getAddressId());

        // address need to changed
        Optional<Address> addressOptional = addressRepository.findByIdAndStatusIsNot(addressId, STATUS_DELETED);
        if (!addressOptional.isPresent()) {
            logger.debug("address = " + addressId + " not found");
            return addressDetailDtos;
        }
        Address address = addressOptional.get();
        if (addressDetailDto.getStatus().equals(STATUS_DELETED)) {
            logger.debug("address already deleted");
            return addressDetailDtos;
        }
        if (!addressDetailDto.getType().equals(address.getType())) {
            logger.debug("type is not the same");
            return addressDetailDtos;
        }
        if (!addressDetailDto.getRelationId().equals(address.getRelationId())) {
            logger.debug("relation id is not the same");
            return addressDetailDtos;
        }
        address.setCity(addressDetailDto.getCityId());
        address.setDescription(addressDetailDto.getDescription());
        address.setProvince(addressDetailDto.getProvinceId());
        address.setPostalCode(addressDetailDto.getPostalCode());
        address.setUpdatedBy(addressDetailDto.getRelationId());
        address.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        address.setStatus(STATUS_DEFAULT);
        // get default address
        Optional<Address> addressDefaultOptional = addressRepository.findByTypeAndRelationIdAndStatusIs(address.getType(), address.getRelationId(), STATUS_DEFAULT);
        if (!addressDefaultOptional.isPresent()) {
            logger.debug("address default not found");
            return addressDetailDtos;
        }
        Address addressDefault = addressDefaultOptional.get();
        addressDefault.setStatus(STATUS_ACTIVE);
        addressDefault.setUpdatedBy(addressDefault.createdBy);
        addressDefault.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        // save default to active first
        addressDefault = addressRepository.save(addressDefault);
        // save active to default
        address = addressRepository.save(address);
        List<Address> addresses = addressRepository.findAllByTypeAndRelationIdAndStatusIsNot(address.getType(), address.getCreatedBy(), STATUS_DELETED);
        if (addresses == null || addresses.size() == 0) {
            logger.debug("Empty user Address");
            return addressDetailDtos;
        }
        for (Address addressNew : addresses) {
            AddressDetailDto addressDetailDtoNew = new AddressDetailDto();
            addressDetailDtoNew.setAddressId(addressNew.getId().toString());
            addressDetailDtoNew.setCityId(addressNew.getCity());
            addressDetailDtoNew.setPostalCode(addressNew.getPostalCode());
            addressDetailDtoNew.setProvinceId(addressNew.getProvince());
            addressDetailDtoNew.setRelationId(addressNew.getRelationId());
            addressDetailDtoNew.setStatus(addressNew.getStatus());
            addressDetailDtoNew.setDescription(addressNew.getDescription());
            addressDetailDtoNew.setType(addressNew.getType());
            try {
                CityParentSingle cityParentSingle = getCitiesByProvinceIdAndCityId(addressNew.getProvince(), addressNew.getCity());
                addressDetailDtoNew.setCityName(cityParentSingle.getRajaongkir().getResults().getCity_name());
                addressDetailDtoNew.setProvinceName(cityParentSingle.getRajaongkir().getResults().getProvince());
            } catch (Exception ex) {
                logger.debug("error get detail ex: " + ex.getMessage());
            }
            addressDetailDtos.add(addressDetailDtoNew);
        }
        return addressDetailDtos;
    }
}
