package com.ankur.OnlineShoppingApp.exception;

public class ProductNotInOrderException extends RuntimeException {
    public ProductNotInOrderException(String message) {
        super(message);
    }
}
