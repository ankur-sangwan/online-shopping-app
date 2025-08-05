package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.resource.WishListItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishListResponseDto {
    private int userId;
    private List<WishListItemDto> itemList = new ArrayList<>();
    private int price;
}
