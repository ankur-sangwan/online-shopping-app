package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.event.OrderReturnedEvent;
import com.ankur.OnlineShoppingApp.event.RefundProcessedEvent;
import com.ankur.OnlineShoppingApp.exception.*;
import com.ankur.OnlineShoppingApp.model.*;
import com.ankur.OnlineShoppingApp.repository.*;
import com.ankur.OnlineShoppingApp.resource.ReturnItemDto;
import com.ankur.OnlineShoppingApp.resource.ReturnRequestDto;
import com.ankur.OnlineShoppingApp.response.ReturnResponseDto;
import com.ankur.OnlineShoppingApp.response.ReturnedProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderReturnService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WalletService walletService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private ReturnItemRepository returnItemRepository;
    @Autowired
    private ReturnEntityRepository returnEntityRepository;

    public ReturnResponseDto handleReturn(ReturnRequestDto request, int userId) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        if (order.getStatus() == OrderStatus.RETURNED) {
            throw new OrderAlreadyReturnedException("This order has already been returned");
        }
        // Jo products order me the unki ek map banao
        Map<Integer, Integer> originalItems = new HashMap<>();
        for (OrderItem item : order.getOrderItems()) {
            int productId = item.getProduct().getId();
            int quantity = item.getQuantity();
            originalItems.put(productId, quantity);
        }
        //  Get already returned quantity till now
        List<ReturnItem> pastReturns = returnItemRepository.findByReturnEntityOrderId(order.getId());
        Map<Integer, Integer> alreadyReturnedQtyMap = new HashMap<>();
        for (ReturnItem items : pastReturns) {
            int productId = items.getProduct().getId();
            int currentQty = alreadyReturnedQtyMap.getOrDefault(productId, 0);
            alreadyReturnedQtyMap.put(productId, currentQty + items.getQuantity());

        }

        // map for product and quantity to remove now
        Map<Integer, Integer> returnItemsMap = new HashMap<>();
        // full return
        boolean isFullReturn = false;
        if (request.getItemsToReturn() == null || request.getItemsToReturn().isEmpty()) {
            returnItemsMap.putAll(originalItems);
        }
        // partial return
        else {
            for (ReturnItemDto item : request.getItemsToReturn()) {
                int productId = item.getProductId();
                int quantityToReturn = item.getQuantity();  // 5
                if (!originalItems.containsKey(productId)) {
                    throw new ProductNotInOrderException("Product ID " + productId + " was not in the original order");
                }

                int originalQty = originalItems.get(productId);   // 8
                int alreadyReturned = alreadyReturnedQtyMap.getOrDefault(productId, 0);  // 1
                int newTotalReturned = alreadyReturned + quantityToReturn;


                if (quantityToReturn <= 0 || newTotalReturned > originalQty) {
                    throw new InvalidReturnQuantityException("Invalid return quantity for product ID: " + productId);
                }
                returnItemsMap.put(productId, quantityToReturn);
                alreadyReturnedQtyMap.put(productId, newTotalReturned);
            }
            //  Final check: is this now a full return?
            isFullReturn = true;
            for (Map.Entry<Integer, Integer> entry : originalItems.entrySet()) {
                int productId = entry.getKey();
                int totalOrdered = entry.getValue();
                int totalReturned = alreadyReturnedQtyMap.getOrDefault(productId, 0);

                if (totalReturned < totalOrdered) {
                    isFullReturn = false;
                    break;
                }
            }
        }
        ReturnEntity returnEntity = new ReturnEntity();
        returnEntity.setOrder(order);
        returnEntity.setUserId(userId);
        returnEntity.setCreatedAt(LocalDateTime.now());
        returnEntity.setFullReturn(isFullReturn);
        List<ReturnItem> returnItemsToSave = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : returnItemsMap.entrySet()) {
            int productId = entry.getKey();
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
            ReturnItem returnItem = new ReturnItem();
            returnItem.setProduct(product);
            returnItem.setQuantity(entry.getValue());
            returnItem.setReturnEntity(returnEntity);
            returnItemsToSave.add(returnItem);
        }
        returnEntity.setReturnItems(returnItemsToSave);
        returnEntityRepository.save(returnEntity);

        // trigger refund
        triggerRefund(userId, order, returnItemsMap);

        for (Map.Entry<Integer, Integer> entry : returnItemsMap.entrySet()) {
            increaseStock(entry.getKey(), entry.getValue());
        }

        //  Update order status
        if (isFullReturn) {
            order.setStatus(OrderStatus.RETURNED);
        } else {
            order.setStatus(OrderStatus.PARTIALLY_RETURNED);
        }

        orderRepository.save(order);
        eventPublisher.publishEvent(
                new OrderReturnedEvent(order.getId(), userId, isFullReturn)
        );
        ReturnResponseDto responseDto = new ReturnResponseDto();
        responseDto.setMessage("The items have been returned and your request has been processed");
        responseDto.setSuccess(true);
        responseDto.setReturnId(returnEntity.getId());
        responseDto.setFullReturn(isFullReturn);
        responseDto.setTimestamp(LocalDateTime.now());
        responseDto.setReturnStatus(order.getStatus());

        List<ReturnedProductDto> returnedProductList = new ArrayList<>();
        for (ReturnItem item : returnItemsToSave) {
            ReturnedProductDto dto = new ReturnedProductDto();
            dto.setProductId(item.getProduct().getId());
            dto.setProductName(item.getProduct().getName());
            dto.setReturnedQuantity(item.getQuantity());
            returnedProductList.add(dto);

        }
        responseDto.setReturnedItems(returnedProductList);


        return responseDto;
    }


    private void triggerRefund(int userId, Order order, Map<Integer, Integer> returnItemsMap) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("user not found"));
        int refundAmount = 0;
        for (OrderItem item : order.getOrderItems()) {
            int productId = item.getProduct().getId();
            if (returnItemsMap.containsKey(productId)) {
                int quantityToReturn = returnItemsMap.get(productId);
                refundAmount += item.getPrice() * quantityToReturn;
            }
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new PaymentProcessingInterruptedException("Payment process interrupted");
        }
        user.setWalletBalance(user.getWalletBalance() + refundAmount);
        userRepository.save(user);

        eventPublisher.publishEvent(
                new RefundProcessedEvent(userId, order.getId(), refundAmount)
        );
    }

    private void increaseStock(int productId, int quantityToIncrease) {

        Product product = productRepository.findById(productId).orElseThrow(() -> new ProductNotFoundException("product not found"));
        product.setQuantity(product.getQuantity() + quantityToIncrease);
        productRepository.save(product);
    }

}
