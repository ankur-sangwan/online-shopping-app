package com.ankur.OnlineShoppingApp.exception;

public class ServiceNotFoundException extends RuntimeException {
    public ServiceNotFoundException(String string) {
        super(string);
    }
}
