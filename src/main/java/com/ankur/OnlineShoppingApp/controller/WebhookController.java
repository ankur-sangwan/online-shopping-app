package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.resource.DeliveryStatusWebhookRequest;
import com.ankur.OnlineShoppingApp.service.WebhookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    @Autowired
    private WebhookService webhookService;
    @PostMapping("/delivery-status")
    public ResponseEntity<String> handleDeliveryWebhook(@RequestBody DeliveryStatusWebhookRequest request,
                                                        @RequestHeader("X-API-KEY") String apiKey) {
        return new ResponseEntity<>(webhookService.handleDeliveryWebhook(request,apiKey), HttpStatus.OK);
    }

}
