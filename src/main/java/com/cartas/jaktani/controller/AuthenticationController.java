package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.CategoryDto;
import com.cartas.jaktani.dto.ParamRequestDto;
import com.cartas.jaktani.service.CategoryService;
import com.cartas.jaktani.service.VwProductDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/authentication")
public class AuthenticationController {
    @Autowired VwProductDetailsService vwProductDetailsService;
    @Autowired CategoryService categoryService;

    @PostMapping(path = "/searchAllProduct")
    public Object searchAllProduct(@RequestBody ParamRequestDto paramRequestDto){
        return vwProductDetailsService.searchAllProduct(paramRequestDto);
    }
    
    @GetMapping(path = "/findProductById/{productId}")
    public Object findProductById(@PathVariable(name = "productId") Integer productId) {
        return vwProductDetailsService.findByProductId(productId);
    }
    
    @GetMapping(path = "/allCategory")
    public Object getAllCategory() {
        return categoryService.getAllCategoryName();
    }

}
