package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Notification;
import com.SkillSwap.SkillSwapMain.services.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    // Send a notification to a user
    @PostMapping
    public ResponseEntity<Notification> sendNotification(@RequestBody Notification notification) {
        return ResponseEntity.ok(notificationService.sendNotification(notification));
    }

    // Get all unread notifications for a user
    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<List<Notification>> getUnreadNotificationsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getUnreadNotificationsForUser(userId));
    }

    // Mark a notification as read
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<Notification> markNotificationAsRead(@PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markNotificationAsRead(notificationId));
    }
}