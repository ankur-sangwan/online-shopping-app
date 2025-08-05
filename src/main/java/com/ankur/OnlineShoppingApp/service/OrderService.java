package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.event.*;
import com.ankur.OnlineShoppingApp.exception.CustomValidationException;
import com.ankur.OnlineShoppingApp.exception.ProductNotFoundException;
import com.ankur.OnlineShoppingApp.exception.ResourceNotFoundException;
import com.ankur.OnlineShoppingApp.model.*;
import com.ankur.OnlineShoppingApp.repository.CartItemRepository;
import com.ankur.OnlineShoppingApp.repository.OrderRepository;
import com.ankur.OnlineShoppingApp.repository.ProductRepository;
import com.ankur.OnlineShoppingApp.repository.UserRepository;
import com.ankur.OnlineShoppingApp.response.OrderHistoryDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.support.TransactionSynchronization;
import com.ankur.OnlineShoppingApp.resource.OrderItemDto;
import com.ankur.OnlineShoppingApp.response.OrderResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;


    @Transactional
    public OrderResponseDto placeAllItemsFromCart(int userId) {
        try {
            Users users = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            List<CartItem> cartItems = cartItemRepository.findCartItems(userId);
            if (cartItems.isEmpty()) {
                throw new CustomValidationException("Cart is empty, cannot place order.");
            }

            //  Stock check for each cart item
            for (CartItem item : cartItems) {
                Product product = item.getProduct();
                if (item.getQuantity() > product.getQuantity()) {
                    throw new CustomValidationException("Insufficient stock for product: " + product.getName());
                }
            }
            Order order = new Order();
            order.setUser(users);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PLACED);
            int total = 0;


            for (CartItem item : cartItems) {
                // update the stock
                Product product = item.getProduct();
                int updatedStock = product.getQuantity() - item.getQuantity();
                product.setQuantity(updatedStock);
                productRepository.save(product);

                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setQuantity(item.getQuantity());
                orderItem.setProduct(item.getProduct());
                orderItem.setPrice(item.getProduct().getPrice());
                total += item.getQuantity() * item.getProduct().getPrice();
                order.getOrderItems().add(orderItem);
            }
            orderRepository.save(order);
            cartItemRepository.deleteAll(cartItems);


            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (OrderItem orderItem : order.getOrderItems()) {
                OrderItemDto orderItemDto = new OrderItemDto();
                orderItemDto.setQuantity(orderItem.getQuantity());
                orderItemDto.setProductName(orderItem.getProduct().getName());
                orderItemDto.setPricePerUnit(orderItem.getPrice());
                orderItemDtoList.add(orderItemDto);

            }

            OrderResponseDto responseDto = new OrderResponseDto();
            responseDto.setOrderId(order.getId());
            responseDto.setTotalAmount(total);
            responseDto.setOrderDate(order.getOrderDate());
            responseDto.setStatus(OrderStatus.PLACED);
            responseDto.setItems(orderItemDtoList);

            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    eventPublisher.publishEvent(new OrderSuccessEvent(userId, order.getId()));
                }
            });
            if (total > 1000000000) {
                throw new RuntimeException("Simulated crash");
            }

            return responseDto;
        }

        // if order fails
        catch (Exception ex) {
            eventPublisher.publishEvent(new OrderFailedEvent(userId, ex.getMessage()));
            throw ex;
        }

    }

    @Transactional
    public OrderResponseDto placeBuyNowOrder(int userId, int productId, int quantity) {
        try {
            Users user = userRepository.findById(userId)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ProductNotFoundException("Product not found"));
            if (quantity > product.getQuantity()) {
                throw new IllegalArgumentException("Requested quantity exceeds available stock");
            }
            // Reduce stock
            product.setQuantity(product.getQuantity() - quantity);
            productRepository.save(product);

            Order order = new Order();
            order.setUser(user);
            order.setOrderDate(LocalDateTime.now());
            order.setStatus(OrderStatus.PLACED);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice());

            order.getOrderItems().add(orderItem);

            Order savedOrder = orderRepository.save(order);

            List<OrderItemDto> orderItemDtoList = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                OrderItemDto dto = new OrderItemDto();
                dto.setQuantity(item.getQuantity());
                dto.setProductName(item.getProduct().getName());
                dto.setPricePerUnit(item.getPrice());
                orderItemDtoList.add(dto);
            }

            OrderResponseDto responseDto = new OrderResponseDto();
            responseDto.setOrderId(savedOrder.getId());
            responseDto.setOrderDate(savedOrder.getOrderDate());
            responseDto.setStatus(savedOrder.getStatus());
            responseDto.setTotalAmount(product.getPrice() * quantity);
            responseDto.setItems(orderItemDtoList);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    eventPublisher.publishEvent(new OrderSuccessEvent(userId, order.getId()));
                }
            });

            return responseDto;
        }
        // if order fails
        catch (Exception ex) {
            eventPublisher.publishEvent(new OrderFailedEvent(userId, ex.getMessage()));
            throw ex;
        }
    }

    public List<OrderHistoryDto> getOrderHistoryForUser(int userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        List<OrderHistoryDto> orderHistoryDtoList = new ArrayList<>();

        for (Order order : orders) {
            List<OrderItemDto> orderItems = new ArrayList<>();
            for (OrderItem item : order.getOrderItems()) {
                OrderItemDto orderItemDto = new OrderItemDto();
                orderItemDto.setPricePerUnit(item.getPrice());
                orderItemDto.setQuantity(item.getQuantity());
                orderItemDto.setProductName(item.getProduct().getName());
                orderItems.add(orderItemDto);
            }
            OrderHistoryDto historyDto = new OrderHistoryDto();
            historyDto.setItems(orderItems);
            historyDto.setOrderId(order.getId());
            historyDto.setCreatedAt(order.getOrderDate());
            historyDto.setStatus(order.getStatus());
            orderHistoryDtoList.add(historyDto);
        }
        return orderHistoryDtoList;

    }

    @Transactional
    public String cancelOrder(int orderId, int userId) {
        try {
            Order order = orderRepository.findById(orderId).orElseThrow(() -> new ProductNotFoundException("order not found"));

            if (order.getUser().getId() != userId) {
                throw new AccessDeniedException("You can cancel only your own orders.");
            }

            if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
                throw new IllegalStateException("Order cannot be cancelled after delivery or if already cancelled.");
            }

            //  Restore stock
            for (OrderItem item : order.getOrderItems()) {
                Product product = item.getProduct();
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            }
            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);
            // refund kr do agr , prepaid h to
            List<Payment> payments=order.getPayments();
            Payment sucessfulPayment=null;
            for(Payment payment:payments){
                if(payment.getPaymentStatus()==PaymentStatus.SUCCESS){
                    sucessfulPayment=payment;
                    break;
                }
            }
            if(sucessfulPayment!=null){
                int refundAmount=sucessfulPayment.getAmount();
                Users user=order.getUser();
                user.setWalletBalance(user.getWalletBalance() + refundAmount);
                userRepository.save(user);
            }
            //   FIRE SUCCESS EVENT
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    // Spring background me dekhta hai:
                    //
                    //"Koi @EventListener hai kya jo OrderCancelledEvent ya OrderCancelFailedEvent sun raha ho?"
                    eventPublisher.publishEvent(new OrderCancelledEvent(userId, orderId));
                }
            });
            return "Order cancelled successfully";
        }
        //  FIRE FAILURE EVENT

        catch (Exception ex) {
            eventPublisher.publishEvent(new OrderCancelFailedEvent(userId, orderId, ex.getMessage()));
            throw ex;
        }

    }

    @Transactional
    public String updateOrderStatus(int orderId, OrderStatus newOrderStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("order not found"));
        OrderStatus oldStatus = order.getStatus();
        if (oldStatus == OrderStatus.DELIVERED || oldStatus == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update status after delivery or cancellation");
        }
        order.setStatus(newOrderStatus);
        orderRepository.save(order);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                // Spring background me dekhta hai:
                //
                //"Koi @EventListener hai kya jo OrderStatusUpdatedEvent ko sun raha ho?"
                eventPublisher.publishEvent(new OrderStatusUpdatedEvent(order.getUser().getId(), orderId, oldStatus, newOrderStatus));
            }
        });
        return "Order status updated to " + order.getStatus();
    }

    public int calculateOrderPrice(@NotNull int orderId) {
        Order order=orderRepository.findById(orderId).get();
        List<OrderItem> orderItems=order.getOrderItems();
        int total=0;
        for(OrderItem items:orderItems){
            total+=items.getPrice()*items.getQuantity();
        }
        return total;
    }

}
