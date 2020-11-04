package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.jwt.JwtUserDetailsService;
import com.cartas.jaktani.model.Token;
import com.cartas.jaktani.service.UserService;
import com.cartas.jaktani.util.BaseHeader;
import com.cartas.jaktani.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<?> getUserByID(@RequestHeader Map<String, String> headers, @PathVariable(name = "id") Long id) {
        try {
            BaseHeader baseHeader = Utils.getHeaderValue(headers);
            //check if user eligible to edit
            if (!Utils.isUserEligibleToEdit(baseHeader.getToken(), id.intValue())) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("User not eligible to see other profile", "NOT_OK", "")));
            }

            UserDto user = userService.getUserByID(id);
            if (null == user) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("User not found for this id :: " + id, "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success get user", user, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("Maaf terjadi gangguan", "NOT_OK", "")));
        }
    }

    @GetMapping(path = "/all")
    public List<UserDto> getAllUser() {
        return userService.getUsers();
    }

    @PostMapping(path = "/add")
    public ResponseEntity<?> addUser(@RequestBody UserDto userDto) {
        try {
            UserDto user = userService.addUser(userDto);
            LoginResponse loginResponse = new LoginResponse("", "OK", "Success add user!", user, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @PostMapping(path = "/edit/{id}")
    public ResponseEntity<?> editUserByID(@RequestHeader Map<String, String> headers, @PathVariable(name = "id") Long id,
                                          @RequestBody UserDto userDto) {
        try {
            BaseHeader baseHeader = Utils.getHeaderValue(headers);
            //check if user eligible to edit
            if (!Utils.isUserEligibleToEdit(baseHeader.getToken(), id.intValue())) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("User not eligible to edit other profile", "NOT_OK", "")));
            }

            UserDto user = userService.editUserByID(id, userDto);
            if (null == user) {
                return ResponseEntity.badRequest().body(new ParentResponse(new CommonResponse("", "NOT_OK", "User not found for this id :: " + id)));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success edit profile!", user, new JwtResponse());
            return ResponseEntity.ok().body(new ParentResponse(loginResponse));
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteUserByID(@RequestHeader Map<String, String> headers, @PathVariable(name = "id") Long id) {
        try {
            BaseHeader baseHeader = Utils.getHeaderValue(headers);

            //check if user eligible to edit
            if (!Utils.isUserEligibleToEdit(baseHeader.getToken(), id.intValue())) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("User not eligible to delete other profile", "NOT_OK", "")));
            }

            UserDto user = userService.deleteUserByID(id);
            if (null == user) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("User not found for this id :: " + id, "NOT_OK", "")));
            }

            LoginResponse loginResponse = new LoginResponse("", "OK", "Success delete user!", user, new JwtResponse());
            ParentResponse parentResponse = new ParentResponse(loginResponse);
            return ResponseEntity.ok().body(parentResponse);
        } catch (Exception e) {
            logger.debug("Caught error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }
}
