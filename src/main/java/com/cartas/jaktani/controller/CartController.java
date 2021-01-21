package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/cart")
public class CartController {
    Logger logger = LoggerFactory.getLogger(CartController.class);

    @Autowired
    CartService cartService;

    @RequestMapping(value = "/add_to_cart", method = RequestMethod.POST)
    public ResponseEntity<?> addToCart(@RequestBody AddToCartDtoRequest addToCartDtoRequest) {
        try {
            AddToCartDtoResponse response = cartService.addToCart(addToCartDtoRequest);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("add_to_cart Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/cart_counter", method = RequestMethod.GET)
    public ResponseEntity<?> cartCounter(@RequestParam String userID) {
        try {
            Long userIDLong = Long.parseLong(userID);
            AddToCartDtoResponse response = cartService.getCounter(userIDLong);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("cart_counter Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/remove_cart", method = RequestMethod.POST)
    public ResponseEntity<?> removeCart(@RequestBody RemoveCartDto removeCartDto) {
        try {
            CommonResponse response = cartService.removeCart(removeCartDto);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("remove_cart Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/update_cart", method = RequestMethod.POST)
    public ResponseEntity<?> updateCart(@RequestBody AddToCartDtoRequest addToCartDtoRequest) {
        try {
            CommonResponse response = cartService.updateCart(addToCartDtoRequest);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("update_cart Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/cart_list", method = RequestMethod.GET)
    public ResponseEntity<?> cartList(@RequestParam String userID) {
        try {
            Long userIDLong = Long.parseLong(userID);
            CartListResponse response = cartService.cartList(new CartListDtoRequest(userIDLong));
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("cart_list Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }
}
