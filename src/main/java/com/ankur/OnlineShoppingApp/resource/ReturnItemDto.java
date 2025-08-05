package com.ankur.OnlineShoppingApp.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
    public  class ReturnItemDto {
        @NotNull(message = "Product ID must not be null")
        private int productId;

        @Min(value = 1, message = "Return quantity must be at least 1")
        private int quantity;
    }