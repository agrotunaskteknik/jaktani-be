package com.cartas.jaktani.dto;

import java.util.List;

public class GetAllCityResponseDto extends CommonResponse {
    private List<CityDto> cityDtoList;

    public GetAllCityResponseDto(List<CityDto> cityDtoList) {
        this.cityDtoList = cityDtoList;
    }

    public GetAllCityResponseDto(String errorMessage, String status, String message, List<CityDto> cityDtoList) {
        super(errorMessage, status, message);
        this.cityDtoList = cityDtoList;
    }

    public List<CityDto> getCityDtoList() {
        return cityDtoList;
    }

    public void setCityDtoList(List<CityDto> cityDtoList) {
        this.cityDtoList = cityDtoList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GetAllCityResponseDto{");
        sb.append("cityDtoList=").append(cityDtoList);
        sb.append('}');
        return sb.toString();
    }
}
