package com.ankur.OnlineShoppingApp.resource;

import com.ankur.OnlineShoppingApp.model.OrderStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateRequest {
    private OrderStatus newOrderStatus;
}
