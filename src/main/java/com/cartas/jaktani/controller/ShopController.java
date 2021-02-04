package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @GetMapping(path = "/id/{id}")
    public Object getShopByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return shopService.getShopByID(id);
    }
    
    @GetMapping(path = "/getByUserId/{userId}")
    public Object getShopByUserID(@PathVariable(name = "userId") Integer userId) throws ResourceNotFoundException {
        return shopService.getShopByUserID(userId);
    }

    @GetMapping(path = "/all")
    public Object getAllShop() {
        return shopService.getAllShops();
    }
    
    @GetMapping(path = "/allByStatus/{status}")
    public Object getAllShopByStatus(@PathVariable(name = "status") Integer status) {
        return shopService.getAllShopsByStatus(status);
    }

	@PostMapping(path = "/add") 
	public Object addShop(@RequestBody ShopDto shop) { 
		return shopService.addShop(shop); 
	}

	@PostMapping(path = "/edit") 
	public Object editShop(@RequestBody ShopDto shop) { 
		return shopService.editShop(shop); 
	}

    @PostMapping(path = "/delete/{id}")
    public Object deleteShopByID(@PathVariable(name = "id") Integer id) throws ResourceNotFoundException {
        return shopService.deleteShopByID(id);
    }
    
    @PostMapping(path = "/updateShopStatusByID/{id}/{status}")
    public Object updateShopStatusByID(@PathVariable(name = "id") Integer id, @PathVariable(name = "status") Integer status) throws ResourceNotFoundException {
        return shopService.updateShopStatusByID(id, status);
    }

}
