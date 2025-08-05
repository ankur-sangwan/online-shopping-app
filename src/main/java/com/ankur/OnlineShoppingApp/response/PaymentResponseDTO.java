package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.OrderStatus;

import com.ankur.OnlineShoppingApp.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentResponseDTO {
    private String paymentId;
    private int amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime createdAt;
    private int orderId;
    private OrderStatus orderStatus;
}
