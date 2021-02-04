package com.cartas.jaktani.service;

import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.model.Users;
import com.cartas.jaktani.repository.UserRepository;
import com.cartas.jaktani.repository.wrapper.UserWrapper;
import com.cartas.jaktani.util.Utils;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	
    @Autowired
    private UserRepository userRepository;

    Integer USER_TYPE_DEFAULT = 1;


    public static final Integer USER_STATUS_DELETED = 0;
    public static final Integer USER_STATUS_ACTIVE = 1;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        userSaved.setPassword(passwordEncoder.encode(userDto.getPassword()));
        // check if user already exist or not
        Optional<Users> usersOptional = userRepository.findByUsernameOrEmailAndStatus(userDto.getUsername(), userDto.getEmail(), USER_STATUS_ACTIVE);
        if (usersOptional.isPresent()) {
            return null;
        }
        Users user = userRepository.save(userSaved);

        return UserWrapper.wrapModelToDto(user);
    }

    @Override
    public UserDto getUserByID(Long userID) {
        Optional<Users> user = userRepository.findById(Integer.valueOf(userID.toString()));
        return user.map(UserWrapper::wrapModelToDto).orElse(null);
    }

    @Override
    public UserDto getUserByUsername(String username) {
        Optional<Users> user = userRepository.findByUsernameAndStatus(username, USER_STATUS_ACTIVE);
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
        Optional<Users> user = userRepository.findByIdAndStatus(userID.intValue(), USER_STATUS_ACTIVE);
        if (!user.isPresent()) {
            return null;
        }
        user.get().setStatus(USER_STATUS_DELETED);
        return UserWrapper.wrapModelToDto(userRepository.save(user.get()));
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
        userSaved.setPassword(userOptional.get().getPassword());
        userSaved.setFullName(userOptional.get().getFullName());
        Users user = userRepository.save(userSaved);

        return UserWrapper.wrapModelToDto(user);
    }

    @Override
    public UserDto editPasswordByUserID(Long userID, UserDto inputUser) {
        Optional<Users> userOptional = userRepository.findById(Integer.valueOf(userID.toString()));
        if (!userOptional.isPresent()) {
            return null;
        }
        if (null == inputUser || inputUser.getPassword().trim().equalsIgnoreCase("") || inputUser.getEmail().trim().equalsIgnoreCase("")) {
            return null;
        }
        inputUser = fillNullValue(inputUser,userOptional.get());
        Users userSaved = UserWrapper.wrapDtoToModel(inputUser);
        userSaved.setCreatedBy(userOptional.get().getCreatedBy());
        userSaved.setCreatedTime(userOptional.get().getCreatedTime());
        userSaved.setUpdatedBy(userOptional.get().getId());
        userSaved.setUpdatedTime(Utils.getTimeStamp(Utils.getCalendar().getTimeInMillis()));
        userSaved.setPassword(passwordEncoder.encode(inputUser.getPassword()));
        userSaved.setFullName(userOptional.get().getFullName());
        Users user = userRepository.save(userSaved);

        return UserWrapper.wrapModelToDto(user);
    }
    
    @Override
    public byte[] getKtpFile(String urlPath) {
    	Optional<Users> userEntity = userRepository.findFirstByKtpUrlPathAndStatusIsNot(urlPath, USER_STATUS_DELETED);
        if (!userEntity.isPresent()) {
            logger.debug("Image Ktp not found from repository");
            return null;
        }
        String imageFilePath = userEntity.get().getKtpFilePath();
        if (imageFilePath == null || imageFilePath.trim().equalsIgnoreCase("")) {
            logger.debug("Image Ktp from repository is empty");
        }
        try {
            File initialFileImage = new File(imageFilePath);
            if (!initialFileImage.exists()) {
                logger.debug("initialFileImage Ktp Path not found");
                return null;
            }
            InputStream in = new FileInputStream(initialFileImage);
            return IOUtils.toByteArray(in);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }
    
    @Override
    public byte[] getProfileFile(String urlPath) {
    	Optional<Users> userEntity = userRepository.findFirstByProfileUrlPathAndStatusIsNot(urlPath, USER_STATUS_DELETED);
        if (!userEntity.isPresent()) {
            logger.debug("Image Profile not found from repository");
            return null;
        }
        String imageFilePath = userEntity.get().getProfileFilePath();
        if (imageFilePath == null || imageFilePath.trim().equalsIgnoreCase("")) {
            logger.debug("Image Profile from repository is empty");
        }
        try {
            File initialFileImage = new File(imageFilePath);
            if (!initialFileImage.exists()) {
                logger.debug("initialFileImage Profile Path not found");
                return null;
            }
            InputStream in = new FileInputStream(initialFileImage);
            return IOUtils.toByteArray(in);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return null;
    }

    public static UserDto fillNullValue(UserDto userDto, Users user){
        if(userDto.getId() == null || userDto.getId() == 0){
            userDto.setId(user.getId());
        }
        if(userDto.getStatus() == null || userDto.getStatus() == 0){
            userDto.setStatus(user.getStatus());
        }
        if(userDto.getType() == null || userDto.getType() == 0){
            userDto.setType(user.getType());
        }
        if(userDto.getUsername() == null || userDto.getUsername().trim().equalsIgnoreCase("")){
            userDto.setUsername(user.getUsername());
        }
        if(userDto.getEmail() == null || userDto.getEmail().trim().equalsIgnoreCase("")){
            userDto.setEmail(user.getEmail());
        }
        if(userDto.getPassword() == null || userDto.getPassword().trim().equalsIgnoreCase("")){
            userDto.setPassword(user.getPassword());
        }
        if(userDto.getFullName() == null || userDto.getFullName().trim().equalsIgnoreCase("")){
            userDto.setFullName(user.getFullName());
        }
        if(userDto.getMobilePhoneNumber() == null || userDto.getMobilePhoneNumber().trim().equalsIgnoreCase("")){
            userDto.setMobilePhoneNumber(user.getMobilePhoneNumber());
        }
        if(userDto.getGender() == null || userDto.getGender() == 0){
            userDto.setGender(user.getGender());
        }
        if(userDto.getBirthDate() == null){
            userDto.setBirthDate(user.getBirthDate());
        }
        return userDto;
    }
}
