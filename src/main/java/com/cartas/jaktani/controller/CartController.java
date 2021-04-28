package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @RequestMapping(value = "/update_cart_v2", method = RequestMethod.POST)
    public ResponseEntity<?> updateCartV2(@RequestBody List<AddToCartDtoRequest> addToCartDtoRequest) {
        try {
            CommonResponse response = cartService.updateCartV2(addToCartDtoRequest);
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

    @RequestMapping(value = "/shipment_address_form", method = RequestMethod.GET)
    public ResponseEntity<?> shipmentAddressForm(@RequestParam String userID) {
        try {
            Long userIDLong = Long.parseLong(userID);
            SAFDtoResponse response = cartService.shipmentAddressForm(new CartListDtoRequest(userIDLong));
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("shipment_address_form Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public ResponseEntity<?> checkout(@RequestBody CheckoutDtoRequest checkoutDtoRequest) {
        try {
            CheckoutDtoResponse response = cartService.checkout(checkoutDtoRequest);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("checkout Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/payment/charge", method = RequestMethod.POST)
    public ResponseEntity<?> paymentCharge(@RequestBody PaymentChargeRequest paymentChargeRequest) {
        try {
            PaymentChargeDtoResponse response = cartService.paymentCharge(paymentChargeRequest);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            logger.debug("paymentCharge Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/payment/status", method = RequestMethod.GET)
    public ResponseEntity<?> paymentStatus(@RequestParam(value = "order_id") String orderId) {
        try {
            PaymentChargeDtoResponse response = cartService.paymentCheckStatus(orderId);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("paymentStatus Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }
}
