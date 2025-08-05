package com.ankur.OnlineShoppingApp.resource;

import lombok.Data;

@Data
public class OrderItemDto {
    private String productName;
    private int quantity;
    private double pricePerUnit;
}