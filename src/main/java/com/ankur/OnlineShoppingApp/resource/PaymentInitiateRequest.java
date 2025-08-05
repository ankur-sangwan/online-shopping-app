package com.ankur.OnlineShoppingApp.resource;

import com.ankur.OnlineShoppingApp.model.PaymentMode;
import com.ankur.OnlineShoppingApp.model.PaymentProvider;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentInitiateRequest {

    @NotNull
    private int orderId;

    @NotNull
    private PaymentMode paymentMode;

    private PaymentProvider provider;


}
