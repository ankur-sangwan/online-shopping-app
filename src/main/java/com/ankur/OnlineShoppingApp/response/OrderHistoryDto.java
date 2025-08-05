package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.OrderStatus;
import com.ankur.OnlineShoppingApp.resource.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderHistoryDto {
    private int orderId;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private List<OrderItemDto> items;
}
