package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.AddressDetailDto;
import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.AddressService;
import com.cartas.jaktani.service.AddressServiceImpl;
import com.cartas.jaktani.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping(path = "/id/{address_id}")
    public Object getByAddressId(@PathVariable(name = "address_id") Integer addressId) {
        return addressService.getByAddressId(addressId);
    }

    @GetMapping(path = "/all/shop/{shop_id}")
    public Object getAllShop(@PathVariable(name = "shop_id") Integer shopId) {
        return addressService.getAllShopAddresses(shopId);
    }

    @GetMapping(path = "/all/user/{user_id}")
    public Object getAllUser(@PathVariable(name = "user_id") Integer userId) {
        return addressService.getAllShopAddresses(userId);
    }


    @PostMapping(path = "/add/user")
    public Object addUserAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        return addressService.saveAddress(addressDetailDto);
    }

    @PostMapping(path = "/add/shop")
    public Object addShopAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        return addressService.saveAddress(addressDetailDto);
    }

    @PostMapping(path = "/edit/shop")
    public Object editShopAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        return addressService.updateAddress(addressDetailDto);
    }

    @PostMapping(path = "/edit/user")
    public Object editUserAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        return addressService.updateAddress(addressDetailDto);
    }

    @PostMapping(path = "/delete/shop")
    public Object deleteShopByID(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        return addressService.updateAddress(addressDetailDto);
    }

    @PostMapping(path = "/delete/user")
    public Object deleteUserByID(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        return addressService.updateAddress(addressDetailDto);
    }
}
