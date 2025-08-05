package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.OrderNotFoundException;
import com.ankur.OnlineShoppingApp.exception.UnauthorizedException;
import com.ankur.OnlineShoppingApp.model.Order;
import com.ankur.OnlineShoppingApp.model.OrderStatus;
import com.ankur.OnlineShoppingApp.model.PaymentStatus;
import com.ankur.OnlineShoppingApp.repository.OrderRepository;
import com.ankur.OnlineShoppingApp.repository.PaymentRepository;
import com.ankur.OnlineShoppingApp.resource.OrderStatusWebhookRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderStatusWebhookService {
    private static final String API_KEY = "super-secret-payment-key";
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    public String updateOrderStatus(@Valid OrderStatusWebhookRequest request, String apiKey) {
        if(!API_KEY.equals(apiKey)){
            throw new UnauthorizedException("Invalid key");
        }
        Order order=paymentRepository.findOrderByPaymentId(request.getPaymentId());
        if(order==null){
            throw new OrderNotFoundException("No order found for payment ID: " + request.getPaymentId());

        }

        if(request.getStatus()== PaymentStatus.SUCCESS){
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
        }
        return "Order status updated successfully to "+order.getStatus();
    }
}
