package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    List<UserDto> listUser = new ArrayList<>();
    Long counterID = 1l;

    @Override
    public UserDto addUser(UserDto userDto) {
        if (null == userDto || userDto.getFullName().trim().equalsIgnoreCase("")) {
            return userDto;
        }
        userDto.setId(counterID);
        listUser.add(userDto);
        counterID++;
        return userDto;
    }

    @Override
    public UserDto getUserByID(Long userID) {
        for (UserDto userDto : listUser) {
            if (userDto.getId().equals(userID)) {
                return userDto;
            }
        }
        return new UserDto();
    }

    @Override
    public List<UserDto> getUsers() {
        return listUser;
    }

    @Override
    public UserDto deleteUserByID(Long userID) {
        UserDto userDto = new UserDto();
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getId().equals(userID)) {
                userDto = listUser.get(i);
                listUser.remove(i);
                break;
            }
        }
        return userDto;
    }

    @Override
    public UserDto editUserByID(Long userID, UserDto inputUser) {
        for (int i = 0; i < listUser.size(); i++) {
            if (listUser.get(i).getId().equals(userID)) {
                listUser.set(i, inputUser);
                break;
            }
        }
        return inputUser;
    }
}
