package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.JwtResponse;
import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.model.Token;
import com.cartas.jaktani.repository.TokenRepository;
import com.cartas.jaktani.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {
    @Autowired
    TokenRepository tokenRepository;

    @Override
    public JwtResponse save(String token, UserDto userDto, String deviceID) throws Exception {
        //check token, user id not null
        if (token.trim().equalsIgnoreCase("") || userDto == null ||
                userDto.getId() == null || userDto.getId() == 0) {
            throw new ResourceNotFoundException("error field empty");
        }
        Optional<Token> tokenOptional = tokenRepository.findByUserID(userDto.getId());
        Token savedToken = new Token();
        savedToken.setToken(token);
        savedToken.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        savedToken.setLastRequest(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        savedToken.setDeviceID(deviceID);
        savedToken.setUserID(userDto.getId());
        //if there any saved token with user id then update it
        if (tokenOptional.isPresent()) {
            savedToken.setId(tokenOptional.get().getId());
            savedToken.setCreatedTime(tokenOptional.get().getCreatedTime());
            savedToken.setLastRequest(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        }
        savedToken = tokenRepository.save(savedToken);
        JwtResponse tokenResponse = new JwtResponse();
        tokenResponse.setLastRequest(savedToken.getLastRequest().getTime());
        tokenResponse.setAccessToken(savedToken.getToken());
        return tokenResponse;
    }
}
