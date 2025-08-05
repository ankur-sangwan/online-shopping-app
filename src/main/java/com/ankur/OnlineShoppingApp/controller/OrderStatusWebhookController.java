package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.resource.OrderStatusWebhookRequest;
import com.ankur.OnlineShoppingApp.service.OrderStatusWebhookService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/webhook")
public class OrderStatusWebhookController {

    private static final String API_KEY = "super-secret-payment-key";

    @Autowired
    private OrderStatusWebhookService webhookService;

    @PostMapping("/update-status")
    public ResponseEntity<String> updateOrderStatusFromWebhook( @Valid @RequestBody OrderStatusWebhookRequest request,@RequestHeader("X-API-KEY") String apiKey){

        return new ResponseEntity<>(webhookService.updateOrderStatus(request,apiKey),HttpStatus.OK);
    }
}

