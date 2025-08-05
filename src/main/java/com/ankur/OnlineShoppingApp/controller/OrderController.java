package com.ankur.OnlineShoppingApp.controller;

import com.ankur.OnlineShoppingApp.model.UserPrincipal;
import com.ankur.OnlineShoppingApp.resource.BuyNowOrderRequestDto;
import com.ankur.OnlineShoppingApp.resource.OrderStatusUpdateRequest;
import com.ankur.OnlineShoppingApp.resource.ReturnRequestDto;
import com.ankur.OnlineShoppingApp.response.ApiResponse;
import com.ankur.OnlineShoppingApp.response.OrderHistoryDto;
import com.ankur.OnlineShoppingApp.response.OrderResponseDto;
import com.ankur.OnlineShoppingApp.response.ReturnResponseDto;
import com.ankur.OnlineShoppingApp.service.OrderReturnService;
import com.ankur.OnlineShoppingApp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderReturnService orderReturnService;

    @PostMapping("/create/all")
    public ResponseEntity<OrderResponseDto> placeAllCartItemsOrder(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();

        return new ResponseEntity<>(
                orderService.placeAllItemsFromCart(userId),
                HttpStatus.CREATED
        );
    }
    @PostMapping("/buy-now")
    public ResponseEntity<OrderResponseDto> placeBuyNowOrder(@RequestBody BuyNowOrderRequestDto request,
                                                             @AuthenticationPrincipal UserPrincipal userPrincipal) {
        int userId = userPrincipal.getId();
        return new ResponseEntity<>(
                orderService.placeBuyNowOrder(userId, request.getProductId(), request.getQuantity()),
                HttpStatus.CREATED
        );
    }
    @GetMapping("/history")
    public ResponseEntity<List<OrderHistoryDto>> orderHistory(@AuthenticationPrincipal UserPrincipal userPrincipal) {

        int userId = userPrincipal.getId();
        return new ResponseEntity<>(orderService.getOrderHistoryForUser(userId),  HttpStatus.OK);

    }
    @PutMapping("/orderCancel/{orderId}")
    public ResponseEntity<String> cancelOrder(@PathVariable int orderId,@AuthenticationPrincipal UserPrincipal userPrincipal) {

        int userId = userPrincipal.getId();
        return new ResponseEntity<>(orderService.cancelOrder(orderId,userId),HttpStatus.OK);
    }
    @PutMapping("/statusUpdate/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateOrderStatus(@PathVariable int orderId, @RequestBody OrderStatusUpdateRequest request) {
        return new ResponseEntity<>(orderService.updateOrderStatus(orderId,request.getNewOrderStatus()),HttpStatus.OK);
    }
    @PostMapping("/return")
    public ResponseEntity<ApiResponse<ReturnResponseDto>> processReturn(@RequestBody ReturnRequestDto request, @AuthenticationPrincipal UserPrincipal  userPrincipal) {
        int userId=userPrincipal.getId();
        return new ResponseEntity<>(new ApiResponse<>("Return processed Successfully",orderReturnService.handleReturn(request,userId),HttpStatus.OK),HttpStatus.OK);
    }
}
