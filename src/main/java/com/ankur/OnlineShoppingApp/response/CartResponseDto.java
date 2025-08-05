package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.resource.CartItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartResponseDto {
    private int userId;

    private List<CartItemDto> itemList = new ArrayList<>();
    private int totalAmount;
}

