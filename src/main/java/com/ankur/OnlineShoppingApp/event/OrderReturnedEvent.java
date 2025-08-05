package com.ankur.OnlineShoppingApp.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderReturnedEvent {

    private  int orderId;
    private  int userId;
    private  boolean fullReturn;
}