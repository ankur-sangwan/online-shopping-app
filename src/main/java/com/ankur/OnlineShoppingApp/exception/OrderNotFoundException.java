package com.ankur.OnlineShoppingApp.exception;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(String message)
  {
    super(message);
  }
}
