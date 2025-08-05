package com.ankur.OnlineShoppingApp.resource;

import com.ankur.OnlineShoppingApp.model.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderStatusWebhookRequest {

    // this class is for changing the status of order to confirmed accordingly to the payment sucess
    @NotBlank(message = "Payment ID must not be blank")
    private String paymentId;

    @NotNull(message = "Payment status must be provided")
    private PaymentStatus status; // SUCCESS or FAILED
}
