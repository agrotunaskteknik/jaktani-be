package com.cartas.jaktani.controller;

import com.cartas.jaktani.service.VwProductDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/productDetails")
public class VwProductDetailsController {
    @Autowired
    VwProductDetailsService vwProductDetailsService;

    @GetMapping(path = "/findByProductId/{id}")
    public Object getProductByID(@PathVariable(name = "id") Integer productId){
        return vwProductDetailsService.findByProductId(productId);
    }
    
    @GetMapping(path = "/findAllByShopId/{shopId}")
    public Object getAllProductByShopId(@PathVariable(name = "shopId") Integer shopId) {
        return vwProductDetailsService.findAllByShopId(shopId);
    }

}
