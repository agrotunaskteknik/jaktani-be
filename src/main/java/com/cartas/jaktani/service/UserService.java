package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto userDto);
    UserDto getUserByID(Long userID);
    List<UserDto> getUsers();
    UserDto deleteUserByID(Long userID);
    UserDto editUserByID(Long userID, UserDto inputUser);
}
