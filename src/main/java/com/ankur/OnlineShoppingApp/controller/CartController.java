package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.model.UserPrincipal;
import com.ankur.OnlineShoppingApp.model.Users;
import com.ankur.OnlineShoppingApp.resource.AddToCartRequestDto;
import com.ankur.OnlineShoppingApp.response.CartResponseDto;
import com.ankur.OnlineShoppingApp.service.CartService;
import com.ankur.OnlineShoppingApp.service.MyUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/cart")
@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponseDto> addToCart(@RequestBody AddToCartRequestDto requestDto,
                                                     @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        CartResponseDto response = cartService.addToCart(userId, requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/viewCart")
    public ResponseEntity<CartResponseDto> viewCart(@AuthenticationPrincipal UserPrincipal userPrincipal){
        int userId = userPrincipal.getId();
        return new ResponseEntity<>(cartService.viewCart(userId),HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<CartResponseDto> deleteCartItems(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @RequestParam int productId,
                                                           @RequestParam(defaultValue = "1") int quantityToRemove
                                                           ){
        int userId = userPrincipal.getId();
        return new ResponseEntity<>(cartService.deleteCartItems(userId,productId,quantityToRemove),HttpStatus.OK);
    }
    @DeleteMapping("/clearCart")
    public ResponseEntity<CartResponseDto> clearCart(@AuthenticationPrincipal UserPrincipal userPrincipal){
        int userId = userPrincipal.getId();

        return new ResponseEntity<>(cartService.clearCart(userId),HttpStatus.OK);

    }
}
