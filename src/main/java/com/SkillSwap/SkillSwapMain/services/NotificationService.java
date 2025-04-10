package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.entity.Notification;
import com.SkillSwap.SkillSwapMain.repositories.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate; // For sending WebSocket messages

    // Send a notification to a user
    public Notification sendNotification(Notification notification) {
        Notification savedNotification = notificationRepository.save(notification);

        // Send the notification to the user in real-time via WebSocket
        messagingTemplate.convertAndSendToUser(
                String.valueOf(notification.getUser().getId()), // User ID as the destination
                "/topic/notifications", // Topic for notifications
                savedNotification // Notification payload
        );

        return savedNotification;
    }

    // Get all unread notifications for a user
    public List<Notification> getUnreadNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdAndIsReadFalse(userId);
    }

    // Mark a notification as read
    public Notification markNotificationAsRead(Long notificationId) {
        return notificationRepository.findById(notificationId).map(notification -> {
            notification.setIsRead(true); // Correctly set the read status
            return notificationRepository.save(notification);
        }).orElseThrow(() -> new RuntimeException("Notification not found"));
    }
}