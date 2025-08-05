package com.ankur.OnlineShoppingApp.exception;

public class InsufficientWalletBalanceException extends RuntimeException {
    public InsufficientWalletBalanceException(String message) {
        super(message);
    }
}
