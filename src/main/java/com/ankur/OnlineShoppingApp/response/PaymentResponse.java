package com.ankur.OnlineShoppingApp.response;

import lombok.Data;

@Data
public class PaymentResponse {
    private String paymentId;
    private int amount;
}
