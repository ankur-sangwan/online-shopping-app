package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductResponseDto {
    private int id;
    private double price;
    private String name;
    private String description;
    private int quantity;
    private ProductCategory category;
}
