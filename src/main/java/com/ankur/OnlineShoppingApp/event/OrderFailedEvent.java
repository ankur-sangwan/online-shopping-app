package com.ankur.OnlineShoppingApp.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderFailedEvent {
    private  int userId;
    private  String reason;


}
