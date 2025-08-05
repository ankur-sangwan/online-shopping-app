package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.resource.PaymentInitiateRequest;
import com.ankur.OnlineShoppingApp.response.PaymentResponseDTO;
import com.ankur.OnlineShoppingApp.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@Valid @RequestBody PaymentInitiateRequest request){
       return new ResponseEntity<>(paymentService.initiatePayment(request), HttpStatus.OK);
    }

}
