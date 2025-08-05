package com.ankur.OnlineShoppingApp.exception;

public class PaymentProcessingInterruptedException extends RuntimeException {
    public PaymentProcessingInterruptedException(String message) {
        super(message);
    }
}