package com.ankur.OnlineShoppingApp.response;

import lombok.Data;

@Data
public class ReturnedProductDto {
    private int productId;
    private String productName;
    private int returnedQuantity;

}
