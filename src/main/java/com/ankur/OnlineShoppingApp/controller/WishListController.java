package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.model.UserPrincipal;
import com.ankur.OnlineShoppingApp.resource.AddToCartRequestDto;
import com.ankur.OnlineShoppingApp.resource.AddToWishlistRequestDTO;
import com.ankur.OnlineShoppingApp.resource.MoveToCartRequestDTO;
import com.ankur.OnlineShoppingApp.response.CartResponseDto;
import com.ankur.OnlineShoppingApp.response.WishListResponseDto;
import com.ankur.OnlineShoppingApp.service.WishListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/wish-list")
@RestController
public class WishListController {
    @Autowired
    private WishListService wishListService;
    @PostMapping("/add")
    public ResponseEntity<WishListResponseDto> addToWishlist(@RequestBody AddToWishlistRequestDTO requestDTO,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        WishListResponseDto response = wishListService.addToWishlist(userId,requestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/view")
    public ResponseEntity<WishListResponseDto> viewWishList(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        WishListResponseDto response = wishListService.viewWishList(userId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @DeleteMapping("/delete")
    public ResponseEntity<WishListResponseDto> deleteWishListItems(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                           @RequestParam int productId){
        int userId = userPrincipal.getId();
        return new ResponseEntity<>(wishListService.deleteWishListItems(userId,productId),HttpStatus.OK);
    }
    @DeleteMapping("/clear-Wish-list")
    public ResponseEntity<WishListResponseDto> clearWishList(@AuthenticationPrincipal UserPrincipal userPrincipal){
        int userId = userPrincipal.getId();
        return new ResponseEntity<>(wishListService.clearWishList(userId),HttpStatus.OK);
    }
    @PostMapping("/move-to-cart")
    public ResponseEntity<CartResponseDto> moveToCart(@RequestBody MoveToCartRequestDTO requestDTO,@AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        CartResponseDto response = wishListService.moveToCartFromWishlist(userId,requestDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
