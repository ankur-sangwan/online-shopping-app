package com.ankur.OnlineShoppingApp.resource;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class AddMoneyToWalletRequestDTO {
    @Min(value = 10, message = "Amount must be at least 10")
    private int amount;

}
