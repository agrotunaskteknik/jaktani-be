package com.cartas.jaktani.controller;

import com.cartas.jaktani.dto.*;
import com.cartas.jaktani.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/order")
public class OrderController {
    @Autowired
    CartService cartService;

    @RequestMapping(value = "/order-history/list", method = RequestMethod.GET)
    public ResponseEntity<?> orderTransactionList(@RequestParam(value = "user_id") Long userID) {
        try {
            List<OrderDetailDto> response = cartService.orderStatusByUserID(userID);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("orderTransactionList Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/order-list", method = RequestMethod.GET)
    public ResponseEntity<?> shopOrderList(@RequestParam(value = "shop_id") Long shopID) {
        try {
            List<OrderDetailDto> response = cartService.orderStatusByShopID(shopID);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("shopOrderList Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/verify_order", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyOrder(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyOrder(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyOrder Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/seller/verify_order_shipping", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyOrderShipping(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getResiCode().trim().equalsIgnoreCase("")) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty ResiCode", "NOT_OK", "")));
            }
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyOrderShipping(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyOrderShipping Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/buyer/verify_order_sent", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyOrderSent(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyOrderSent(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyOrderSent Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }

    @RequestMapping(value = "/buyer/verify_review", method = RequestMethod.POST)
    public ResponseEntity<?> sellerVerifyReview(@RequestBody VerifyOrderShippingRequest request) {
        try {
            List<OrderDetailDto> response = new ArrayList<>();
            if (request.getOrderID().equals(0L)) {
                return ResponseEntity.ok().body(new ParentResponse(new CommonResponse("Empty OrderID", "NOT_OK", "")));
            }
            cartService.sellerVerifyReview(request);
            return ResponseEntity.ok().body(new ParentResponse(response));
        } catch (Exception e) {
            System.out.println("sellerVerifyReview Caught Error : " + e.getMessage());
            return ResponseEntity.ok().body(new ParentResponse(new CommonResponse(e.getMessage(), "NOT_OK", "")));
        }
    }


}
