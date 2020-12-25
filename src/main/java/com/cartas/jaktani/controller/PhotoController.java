package com.cartas.jaktani.controller;

import com.cartas.jaktani.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/photo")
public class PhotoController {
    @Autowired
    PhotoService photoService;

    @GetMapping(path = "/getImageByUniqueKey/{path}", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImageByUniqueKey(@PathVariable(value = "path") String path) {
        return photoService.getPhoto(path);
    }
}
