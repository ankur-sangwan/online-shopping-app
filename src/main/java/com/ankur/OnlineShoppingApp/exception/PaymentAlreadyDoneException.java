package com.ankur.OnlineShoppingApp.exception;

public class PaymentAlreadyDoneException extends RuntimeException {
    public PaymentAlreadyDoneException(String message) {
        super(message);
    }
}
