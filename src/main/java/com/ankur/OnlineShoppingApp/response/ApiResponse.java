package com.ankur.OnlineShoppingApp.response;

import org.springframework.http.HttpStatus;

public class ApiResponse<T> {
    public String message;
    public T data;
    public HttpStatus status;

    public ApiResponse(String message, T data, HttpStatus status) {
        this.message = message;
        this.data = data;
        this.status = status;
    }
}
