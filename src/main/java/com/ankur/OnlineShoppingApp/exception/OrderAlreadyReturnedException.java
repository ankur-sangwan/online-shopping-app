package com.ankur.OnlineShoppingApp.exception;

public class OrderAlreadyReturnedException extends RuntimeException {

    public OrderAlreadyReturnedException(String message) {

        super(message);
    }

}
