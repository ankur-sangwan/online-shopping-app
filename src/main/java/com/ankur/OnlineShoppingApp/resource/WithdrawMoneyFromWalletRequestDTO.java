package com.ankur.OnlineShoppingApp.resource;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class WithdrawMoneyFromWalletRequestDTO {
    @Min(value = 10, message = "Withdraw amount must be at least 10")
    private int amount;
}
