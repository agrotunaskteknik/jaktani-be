package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.UserDto;
import com.cartas.jaktani.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping(path = "/id/{id}")
    public UserDto getUserByID(@PathVariable(name = "id") Long id){
        return userService.getUserByID(id);
    }

    @GetMapping(path = "/all")
    public List<UserDto> getAllUser(){
        return userService.getUsers();
    }

    @PostMapping(path = "/add")
    public UserDto addUser(@RequestBody UserDto userDto) {
        return userService.addUser(userDto);
    }

    @PostMapping(path = "/edit/{id}")
    public UserDto editUserByID(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto){
        return userService.editUserByID(id, userDto);
    }

    @PostMapping(path = "/delete/{id}")
    public UserDto deleteUserByID(@PathVariable(name = "id") Long id){
        return userService.deleteUserByID(id);
    }

}
