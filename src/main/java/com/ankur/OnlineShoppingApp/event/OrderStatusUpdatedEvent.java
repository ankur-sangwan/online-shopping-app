package com.ankur.OnlineShoppingApp.event;

import com.ankur.OnlineShoppingApp.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusUpdatedEvent {
    private  int userId;
    private  int orderId;
    private  OrderStatus oldStatus;
    private  OrderStatus newStatus;

}
