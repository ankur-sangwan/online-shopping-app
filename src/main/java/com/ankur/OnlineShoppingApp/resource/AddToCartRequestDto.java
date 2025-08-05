package com.ankur.OnlineShoppingApp.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddToCartRequestDto {
        private int ProductId;
        private int quantity;
}
