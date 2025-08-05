package com.ankur.OnlineShoppingApp.resource;

import com.ankur.OnlineShoppingApp.model.ProductCategory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;
    @Positive(message = "Price must be greater than 0")
    private int price;
    @Min(value = 0, message = "Quantity cannot be negative")
    private int quantity;
    private ProductCategory category;

}