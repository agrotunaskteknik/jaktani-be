package com.cartas.jaktani.controller;

import com.cartas.jaktani.service.PhotoService;
import com.cartas.jaktani.service.ShopService;
import com.cartas.jaktani.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/photo")
public class PhotoController {
    @Autowired
    PhotoService photoService;
    
    @Autowired
    ShopService shopService;
    
    @Autowired
    UserService userService;

    @GetMapping(path = "/getImageByUniqueKey/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageByUniqueKey(@PathVariable(value = "path") String path) {
        return photoService.getPhoto(path);
    }
    
    @GetMapping(path = "/getImageLogoByUniqueKey/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageLogoByUniqueKey(@PathVariable(value = "path") String path) {
        return shopService.getLogoFile(path);
    }
    
    @GetMapping(path = "/getImageKtpByUniqueKey/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageKtpByUniqueKey(@PathVariable(value = "path") String path) {
        return userService.getKtpFile(path);
    }
    
    @GetMapping(path = "/getImageProfileByUniqueKey/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageProfileByUniqueKey(@PathVariable(value = "path") String path) {
        return userService.getProfileFile(path);
    }
}
