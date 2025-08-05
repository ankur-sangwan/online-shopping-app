package com.ankur.OnlineShoppingApp.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private String productName;
    private int pricePerUnit;
    private int quantity;
}
