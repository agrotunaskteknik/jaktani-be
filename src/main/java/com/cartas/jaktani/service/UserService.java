package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    UserDto getUserByID(Long userID);
    UserDto getUserByUsername(String username);
    List<UserDto> getUsers();
    UserDto deleteUserByID(Long userID);
    UserDto editUserByID(Long userID, UserDto inputUser);
    UserDto editPasswordByUserID(Long userID, UserDto inputUser);
    byte[] getKtpFile(String urlPath);
    byte[] getProfileFile(String urlPath);
}
