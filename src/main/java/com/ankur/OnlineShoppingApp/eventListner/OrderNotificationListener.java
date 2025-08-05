package com.ankur.OnlineShoppingApp.eventListner;

import com.ankur.OnlineShoppingApp.event.*;
import com.ankur.OnlineShoppingApp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OrderNotificationListener {
    @Autowired
    private NotificationService notificationService;

    @Async
    @EventListener
    public void handleOrderSuccess(OrderSuccessEvent event) {
        String msg = " Order placed successfully for userId: " + event.getUserId()
                + ", OrderId: " + event.getOrderId();
        notificationService.logNotification(msg);
    }

    @Async
    @EventListener
    public void handleOrderFailed(OrderFailedEvent event) {
        String msg = " Order failed for userId: " + event.getUserId()
                + ", Reason: " + event.getReason();
        notificationService.logNotification(msg);
    }

    @Async
    @EventListener
    public void handleOrderCancel(OrderCancelledEvent event) {
        String msg = " Order cancelled: OrderId = " + event.getOrderId()
                + ", by UserId = " + event.getUserId();
        notificationService.logNotification(msg);
    }
    @Async
    @EventListener
    public void handleCancelledFailedOrder(OrderCancelFailedEvent event) {
        String msg = " Order cancel failed for OrderId = " + event.getOrderId()
                + ", UserId = " + event.getUserId()
                + ", Reason: " + event.getReason();
        notificationService.logNotification(msg);
    }
    @Async
    @EventListener
    public void handleOrderStatusUpdate(OrderStatusUpdatedEvent event) {
        String msg ="Order status updated: OrderId = " + event.getOrderId()
                + ", UserId = " + event.getUserId() + ", " + event.getOldStatus() + " -> " + event.getNewStatus();;
        notificationService.logNotification(msg);
    }
    @Async
    @EventListener
    public void handleOrderReturn(OrderReturnedEvent  event) {
        String msg;

        if (event.isFullReturn()) {
            msg = "Order fully returned: OrderId = " + event.getOrderId()
                    + ", UserId = " + event.getUserId();
        } else {
            msg = "Order partially returned: OrderId = " + event.getOrderId()
                    + ", UserId = " + event.getUserId();
        }
        notificationService.logNotification(msg);
    }
    @Async
    @EventListener
    public void handleRefund(RefundProcessedEvent event) {
        String msg = "Refund of rs . " + event.getAmount() + " processed for Order ID = " +
                event.getOrderId() + ", User ID = " + event.getUserId();
        notificationService.logNotification(msg);
    }


}
