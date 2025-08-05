package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.model.UserPrincipal;
import com.ankur.OnlineShoppingApp.resource.AddMoneyToWalletRequestDTO;
import com.ankur.OnlineShoppingApp.resource.WithdrawMoneyFromWalletRequestDTO;
import com.ankur.OnlineShoppingApp.response.ApiResponse;
import com.ankur.OnlineShoppingApp.response.WalletBalanceResponseDTO;
import com.ankur.OnlineShoppingApp.service.WalletService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wallet")
public class WalletController {

    @Autowired
    private WalletService walletService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<WalletBalanceResponseDTO>> addMoney(@RequestBody @Valid AddMoneyToWalletRequestDTO request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        ApiResponse<WalletBalanceResponseDTO> response = new ApiResponse<>("Money added to wallet successfully", walletService.addMoneyToWallet(request, userId), HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/withDraw")
    public ResponseEntity<ApiResponse<WalletBalanceResponseDTO>> withDrawMoney(@RequestBody @Valid WithdrawMoneyFromWalletRequestDTO request, @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        ApiResponse<WalletBalanceResponseDTO> response = new ApiResponse<>("\"Money withdrawn from wallet successfully", walletService.withDrawMoneyFromWallet(request, userId), HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/balance")
    public ResponseEntity<ApiResponse<WalletBalanceResponseDTO>> getWalletBalance(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        return new ResponseEntity<>(new ApiResponse<>("Wallet balance fetched successfully", walletService.checkWalletBalance(userId), HttpStatus.OK), HttpStatus.OK);
    }
}
