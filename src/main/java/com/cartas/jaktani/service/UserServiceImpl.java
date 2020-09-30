package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.model.Users;
import com.cartas.jaktani.repository.UserRepository;
import com.cartas.jaktani.repository.wrapper.UserWrapper;
import com.cartas.jaktani.util.Utils;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    Integer USER_TYPE_DEFAULT = 1;


    Integer USER_STATUS_DELETED = 0;
    Integer USER_STATUS_ACTIVE = 1;

    @Override
    public UserDto addUser(UserDto userDto) {
        if (null == userDto || userDto.getFullName().trim().equalsIgnoreCase("") || userDto.getUsername().trim().equalsIgnoreCase("")
                || userDto.getPassword().trim().equalsIgnoreCase("") || userDto.getEmail().trim().equalsIgnoreCase("")) {
            return null;
        }
        userDto.setType(USER_TYPE_DEFAULT);
        userDto.setStatus(USER_STATUS_ACTIVE);
        Users userSaved = UserWrapper.wrapDtoToModel(userDto);
        userSaved.setCreatedBy(USER_TYPE_DEFAULT);
        userSaved.setCreatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        Users user = userRepository.save(userSaved);

        return UserWrapper.wrapModelToDto(user);
    }

    @Override
    public UserDto getUserByID(Long userID) {
        Optional<Users> user = userRepository.findById(Integer.valueOf(userID.toString()));
        return user.map(UserWrapper::wrapModelToDto).orElse(null);
    }

    @Override
    public List<UserDto> getUsers() {
        List<Users> users = userRepository.findAll();
        List<UserDto> userDtos = new ArrayList<>();
        for (Users user : users) {
            userDtos.add(UserWrapper.wrapModelToDto(user));
        }
        return userDtos;
    }

    @Override
    public UserDto deleteUserByID(Long userID) {
        Users user = userRepository.getOne(Integer.valueOf(userID.toString()));
        if (user.getId() == 0) {
            return null;
        }
        user.setStatus(USER_STATUS_DELETED);
        return UserWrapper.wrapModelToDto(userRepository.save(user));
    }

    @Override
    public UserDto editUserByID(Long userID, UserDto inputUser) {
        Optional<Users> userOptional = userRepository.findById(Integer.valueOf(userID.toString()));
        if (!userOptional.isPresent()) {
            return null;
        }
        if (null == inputUser || inputUser.getFullName().trim().equalsIgnoreCase("") || inputUser.getUsername().trim().equalsIgnoreCase("")
                || inputUser.getPassword().trim().equalsIgnoreCase("") || inputUser.getEmail().trim().equalsIgnoreCase("")) {
            return null;
        }
        inputUser.setId(userOptional.get().getId());
        inputUser.setStatus(userOptional.get().getStatus());
        inputUser.setType(userOptional.get().getType());
        Users userSaved = UserWrapper.wrapDtoToModel(inputUser);
        userSaved.setCreatedBy(userOptional.get().getCreatedBy());
        userSaved.setCreatedTime(userOptional.get().getCreatedTime());
        userSaved.setUpdatedBy(userOptional.get().getId());
        userSaved.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        Users user = userRepository.save(UserWrapper.wrapDtoToModel(inputUser));

        return UserWrapper.wrapModelToDto(user);
    }
}
