package com.SkillSwap.SkillSwapMain.services;


import com.SkillSwap.SkillSwapMain.entity.Notification;

import java.util.List;

public interface NotificationService {
    Notification sendNotification(Long userId, String message);
    List<Notification> getNotifications(Long userId);
}
