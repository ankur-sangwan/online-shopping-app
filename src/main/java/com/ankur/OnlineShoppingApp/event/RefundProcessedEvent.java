package com.ankur.OnlineShoppingApp.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundProcessedEvent {
    private  int userId;
    private  int orderId;
    private  int amount;
}