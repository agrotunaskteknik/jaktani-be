package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @GetMapping(path = "/authentication/id/{id}")
    public Object getShopByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return shopService.getShopByID(id);
    }

    @GetMapping(path = "/authentication/all")
    public Object getAllShop() {
        return shopService.getAllShops();
    }

	@PostMapping(path = "/add") 
	public Object addShop(@RequestBody ShopDto shop) { 
		return shopService.addShop(shop); 
	}
	 

	@PostMapping(path = "/edit/{id}") 
	public Object editShop(@RequestBody ShopDto shop) { 
		return shopService.editShop(shop); 
	}

    @PostMapping(path = "/delete/{id}")
    public Object deleteShopByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return shopService.deleteShopByID(id);
    }

}
