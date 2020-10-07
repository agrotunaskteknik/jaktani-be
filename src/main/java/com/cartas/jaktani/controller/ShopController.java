package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.model.Shop;
import com.cartas.jaktani.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @GetMapping(path = "/id/{id}")
    public Object getShopByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return shopService.getShopByID(id);
    }

    @GetMapping(path = "/all")
    public Object getAllShop() {
        return shopService.getAllShops();
    }

	
	@PostMapping(path = "/add") 
	public Object addUser(@RequestBody ShopDto shop) { 
		return shopService.addShop(shop); 
	}
	 

	/*
	 * @PostMapping(path = "/edit/{id}") public ResponseEntity<UserDto>
	 * editUserByID(@PathVariable(name = "id") Long id, @RequestBody UserDto
	 * userDto) throws ResourceNotFoundException { UserDto user =
	 * userService.editUserByID(id, userDto); if (null == user) { throw new
	 * ResourceNotFoundException("User not found for this id :: " + id); } return
	 * ResponseEntity.ok().body(user); }
	 */

    @PostMapping(path = "/delete/{id}")
    public Object deleteShopByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return shopService.deleteShopByID(id);
    }

}
