package com.ankur.OnlineShoppingApp.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String string) {
        super(string);
    }
}

