package com.ankur.OnlineShoppingApp.resource;

import lombok.Data;

@Data
public class CartOrderItemDto {
    private int productId;
    private int quantity;
}
