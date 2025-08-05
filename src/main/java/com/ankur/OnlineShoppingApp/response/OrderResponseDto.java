package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.OrderStatus;
import com.ankur.OnlineShoppingApp.resource.OrderItemDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private int orderId;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private List<OrderItemDto> items;
    private int totalAmount;
}
