package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.PaymentMode;

public class PaymentRequest {
    private PaymentMode paymentMode;
    private String provider; // RAZORPAY, PAYTM, etc.
}
