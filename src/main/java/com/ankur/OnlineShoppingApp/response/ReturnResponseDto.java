package com.ankur.OnlineShoppingApp.response;

import com.ankur.OnlineShoppingApp.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnResponseDto {
    private String message;
    private boolean success;
    private int returnId;
    private boolean fullReturn;
    private LocalDateTime timestamp;
    private List<ReturnedProductDto> returnedItems;
    private OrderStatus returnStatus;
}
