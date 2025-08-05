package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.InvalidOrderStateException;
import com.ankur.OnlineShoppingApp.exception.PaymentAlreadyDoneException;
import com.ankur.OnlineShoppingApp.exception.PaymentProcessingInterruptedException;
import com.ankur.OnlineShoppingApp.exception.ResourceNotFoundException;
import com.ankur.OnlineShoppingApp.model.*;
import com.ankur.OnlineShoppingApp.repository.OrderRepository;
import com.ankur.OnlineShoppingApp.repository.PaymentRepository;
import com.ankur.OnlineShoppingApp.resource.PaymentInitiateRequest;
import com.ankur.OnlineShoppingApp.response.PaymentResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    OrderService orderService;

    public PaymentResponseDTO initiatePayment(@Valid PaymentInitiateRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("order not found"));
        if(order.getStatus()==OrderStatus.CANCELLED){
            throw new InvalidOrderStateException("this order is cancelled !! you can not create payment");
        }
        List<Payment> payments=paymentRepository.findByOrderId(order.getId());
        for(Payment payment:payments){
            if (payment.getPaymentStatus()==PaymentStatus.SUCCESS){
                throw new PaymentAlreadyDoneException("payment already done !! you can not do it twice");
            }
        }
        int totalPriceToPay = orderService.calculateOrderPrice(request.getOrderId());
        Payment payment = new Payment();
        payment.setAmount(totalPriceToPay);
        payment.setOrder(order);
        payment.setPaymentMode(request.getPaymentMode());
        if (request.getProvider() == null && request.getPaymentMode() == PaymentMode.COD) {
            payment.setProvider(null);
            payment.setPaymentStatus(PaymentStatus.PENDING);
        } else {
            payment.setProvider(request.getProvider());
            // Dummy success (later real gateway or webhook can set this)
            try{
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new PaymentProcessingInterruptedException("Payment process interrupted");
            }
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
        }

        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        PaymentResponseDTO responseDTO=new PaymentResponseDTO();

        responseDTO.setPaymentId(payment.getPaymentId());
        responseDTO.setOrderId(payment.getOrder().getId());
        responseDTO.setAmount(payment.getAmount());
        responseDTO.setPaymentStatus(payment.getPaymentStatus());
        responseDTO.setCreatedAt(payment.getCreatedAt());
        responseDTO.setOrderStatus(order.getStatus());

        return responseDTO;
    }
}
