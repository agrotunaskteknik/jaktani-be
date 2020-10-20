package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.JwtResponse;
import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.model.Token;

public interface TokenService {
    JwtResponse save(String token, UserDto userDto, String deviceID) throws Exception;
}
