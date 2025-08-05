package com.ankur.OnlineShoppingApp.resource;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
@Data
public class ReturnRequestDto {

    @NotNull(message = "Order ID must not be null")
    private int orderId;

    // Empty or null means full return
    private List<ReturnItemDto> itemsToReturn;



}
