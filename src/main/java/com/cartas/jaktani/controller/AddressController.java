package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.AddressDetailDto;
import com.cartas.jaktani.dto.ShopDto;
import com.cartas.jaktani.exceptions.ResourceNotFoundException;
import com.cartas.jaktani.service.AddressService;
import com.cartas.jaktani.service.AddressServiceImpl;
import com.cartas.jaktani.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(path = "/api/address")
public class AddressController {
    @Autowired
    AddressService addressService;

    @GetMapping(path = "/id/{address_id}")
    public Object getByAddressId(@PathVariable(name = "address_id") Integer addressId) {
        return addressService.getByAddressId(addressId);
    }

    @GetMapping(path = "/shop/default/{shop_id}")
    public Object getShopDefaultByAddressId(@PathVariable(name = "shop_id") Integer shopId) {
        return addressService.getDefaultAddressByIdAndRelationType(shopId, AddressServiceImpl.TYPE_SHOP);
    }

    @GetMapping(path = "/user/default/{user_id}")
    public Object getUserDefaultByAddressId(@PathVariable(name = "user_id") Integer userId) {
        return addressService.getDefaultAddressByIdAndRelationType(userId, AddressServiceImpl.TYPE_USER);
    }

    @GetMapping(path = "/all/shop/{shop_id}")
    public Object getAllShop(@PathVariable(name = "shop_id") Integer shopId) {
        return addressService.getAllShopAddresses(shopId);
    }

    @GetMapping(path = "/all/user/{user_id}")
    public Object getAllUser(@PathVariable(name = "user_id") Integer userId) {
        return addressService.getAllUserAddresses(userId);
    }


    @PostMapping(path = "/add/user")
    public Object addUserAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        List<AddressDetailDto> addressDetailDtoList = addressService.getAllUserAddresses(addressDetailDto.getRelationId());
        for (AddressDetailDto address : addressDetailDtoList) {
            if (address.getStatus().equals(AddressServiceImpl.STATUS_DEFAULT)) {
                addressDetailDto.setStatus(AddressServiceImpl.STATUS_ACTIVE);
                break;
            }
        }
        return addressService.saveAddress(addressDetailDto);
    }

    @PostMapping(path = "/add/shop")
    public Object addShopAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        List<AddressDetailDto> addressDetailDtoList = addressService.getAllShopAddresses(addressDetailDto.getRelationId());
        for (AddressDetailDto address : addressDetailDtoList) {
            if (address.getStatus().equals(AddressServiceImpl.STATUS_DEFAULT)) {
                addressDetailDto.setStatus(AddressServiceImpl.STATUS_ACTIVE);
                break;
            }
        }
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

    @PostMapping(path = "/shop/set_default")
    public Object setDefaultShopAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_SHOP);
        return addressService.setDefaultAddress(addressDetailDto);
    }

    @PostMapping(path = "/user/set_default")
    public Object setDefaultUserAddress(@RequestBody AddressDetailDto addressDetailDto) {
        addressDetailDto.setType(AddressServiceImpl.TYPE_USER);
        return addressService.setDefaultAddress(addressDetailDto);
    }
}
