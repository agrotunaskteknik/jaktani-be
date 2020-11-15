package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.CategoryDto;
import com.cartas.jaktani.dto.ParamRequestDto;
import com.cartas.jaktani.service.VwProductDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {
    @Autowired
    VwProductDetailsService vwProductDetailsService;

    @PostMapping(path = "/searchAllProduct/{id}")
    public Object searchAllProduct(@RequestBody ParamRequestDto paramRequestDto){
        return vwProductDetailsService.searchAllProduct(paramRequestDto);
    }

}
