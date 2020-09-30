package com.cartas.jaktani.repository.wrapper;

import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.model.Users;

public class UserWrapper {
    public static Users wrapDtoToModel(UserDto userDto){
        Users user = new Users();
        user.setId(userDto.getId());
        user.setFullName(userDto.getFullName());
        user.setUsername(userDto.getUsername());
        user.setType(userDto.getType());
        user.setPassword(userDto.getPassword());
        user.setEmail(userDto.getEmail());
        user.setStatus(userDto.getStatus());
        user.setGender(userDto.getGender());
        user.setMobilePhoneNumber(userDto.getMobilePhoneNumber());
        user.setBirthDate(userDto.getBirthDate());
        return user;
    }

    public static UserDto wrapModelToDto(Users user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFullName(user.getFullName());
        userDto.setUsername(user.getUsername());
        userDto.setType(user.getType());
        userDto.setPassword(user.getPassword());
        userDto.setEmail(user.getEmail());
        userDto.setStatus(user.getStatus());
        userDto.setGender(user.getGender());
        userDto.setMobilePhoneNumber(user.getMobilePhoneNumber());
        userDto.setBirthDate(user.getBirthDate());
        return userDto;
    }
}
