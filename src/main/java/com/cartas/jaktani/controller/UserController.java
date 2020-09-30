package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(path = "/id/{id}")
    public ResponseEntity<UserDto> getUserByID(@PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        UserDto user = userService.getUserByID(id);
        if (null == user) {
            throw new ResourceNotFoundException("User not found for this id :: " + id);
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping(path = "/all")
    public List<UserDto> getAllUser() {
        return userService.getUsers();
    }

    @PostMapping(path = "/add")
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PostMapping(path = "/edit/{id}")
    public ResponseEntity<UserDto> editUserByID(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto) throws ResourceNotFoundException {
        UserDto user = userService.editUserByID(id, userDto);
        if (null == user) {
            throw new ResourceNotFoundException("User not found for this id :: " + id);
        }
        return ResponseEntity.ok().body(user);
    }

    @PostMapping(path = "/delete/{id}")
    public ResponseEntity<UserDto> deleteUserByID(@PathVariable(name = "id") Long id) throws ResourceNotFoundException {
        UserDto user = userService.deleteUserByID(id);
        if (null == user) {
            throw new ResourceNotFoundException("User not found for this id :: " + id);
        }
        return ResponseEntity.ok().body(user);
    }

}
