package com.ankur.OnlineShoppingApp.service;

import com.ankur.OnlineShoppingApp.model.Notification;
import com.ankur.OnlineShoppingApp.repository.NotificationRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logNotification(String msg) {
        Notification notification = new Notification(msg, LocalDateTime.now());
        notificationRepository.save(notification);
        System.out.println("Notification saved: " + msg);
    }
}
