package com.cartas.jaktani.dto;

import java.util.List;

public class GetAllProvinceResponseDto extends CommonResponse {
    private List<ProvinceDto> provinceDtoList;

    public GetAllProvinceResponseDto(List<ProvinceDto> provinceDtoList) {
        this.provinceDtoList = provinceDtoList;
    }

    public GetAllProvinceResponseDto(String errorMessage, String status, String message, List<ProvinceDto> provinceDtoList) {
        super(errorMessage, status, message);
        this.provinceDtoList = provinceDtoList;
    }

    public List<ProvinceDto> getProvinceDtoList() {
        return provinceDtoList;
    }

    public void setProvinceDtoList(List<ProvinceDto> provinceDtoList) {
        this.provinceDtoList = provinceDtoList;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GetAllProvinceResponseDto{");
        sb.append("provinceDtoList=").append(provinceDtoList);
        sb.append('}');
        return sb.toString();
    }
}
