package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.exception.InsufficientWalletBalanceException;
import com.ankur.OnlineShoppingApp.model.Users;
import com.ankur.OnlineShoppingApp.repository.UserRepository;
import com.ankur.OnlineShoppingApp.resource.AddMoneyToWalletRequestDTO;
import com.ankur.OnlineShoppingApp.resource.WithdrawMoneyFromWalletRequestDTO;
import com.ankur.OnlineShoppingApp.response.WalletBalanceResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WalletService {
    @Autowired
    private UserRepository userRepository;

    public WalletBalanceResponseDTO checkWalletBalance(int userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        int walletMoney = user.getWalletBalance();
        WalletBalanceResponseDTO responseDTO = new WalletBalanceResponseDTO();
        responseDTO.setUserId(userId);
        responseDTO.setWalletBalance(walletMoney);
        return responseDTO;
    }

    public WalletBalanceResponseDTO addMoneyToWallet(@Valid AddMoneyToWalletRequestDTO request, int userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));

        int newBalance = request.getAmount() + user.getWalletBalance();
        user.setWalletBalance(newBalance);
        userRepository.save(user);
        WalletBalanceResponseDTO responseDTO = new WalletBalanceResponseDTO();
        responseDTO.setUserId(userId);
        responseDTO.setWalletBalance(newBalance);
        return responseDTO;
    }
    public WalletBalanceResponseDTO withDrawMoneyFromWallet(@Valid WithdrawMoneyFromWalletRequestDTO request, int userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        int amountToWithDraw=request.getAmount();
        if(amountToWithDraw>user.getWalletBalance()){
            throw new InsufficientWalletBalanceException("Insufficient wallet balance");
        }
        int newBalance = user.getWalletBalance()-amountToWithDraw;
        user.setWalletBalance(newBalance);
        userRepository.save(user);
        WalletBalanceResponseDTO responseDTO = new WalletBalanceResponseDTO();
        responseDTO.setUserId(userId);
        responseDTO.setWalletBalance(newBalance);
        return responseDTO;
    }
}
