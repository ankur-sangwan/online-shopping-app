package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.UnauthorizedException;
import com.ankur.OnlineShoppingApp.resource.DeliveryStatusWebhookRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WebhookService {

    private static final String API_KEY = "secret-delivery-api-key";
    @Autowired
    private OrderService orderService;

    public String handleDeliveryWebhook(DeliveryStatusWebhookRequest request, String apiKey) {
        if(!API_KEY.equals(apiKey)){
            throw new UnauthorizedException("Invalid API Key");
        }
        return orderService.updateOrderStatus(request.getOrderId(),request.getNewStatus());

    }
}
