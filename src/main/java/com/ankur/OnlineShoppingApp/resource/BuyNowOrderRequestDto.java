package com.ankur.OnlineShoppingApp.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyNowOrderRequestDto {
    private int productId;
    private int quantity;
}