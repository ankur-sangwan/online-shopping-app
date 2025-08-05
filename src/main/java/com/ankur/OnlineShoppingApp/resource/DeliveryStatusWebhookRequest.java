package com.ankur.OnlineShoppingApp.resource;

import com.ankur.OnlineShoppingApp.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryStatusWebhookRequest {
    private int orderId;
    private OrderStatus newStatus;
}
