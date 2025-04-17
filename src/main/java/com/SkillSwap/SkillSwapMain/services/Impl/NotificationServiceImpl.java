package com.SkillSwap.SkillSwapMain.services.Impl;


import com.SkillSwap.SkillSwapMain.entity.Notification;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.repositories.NotificationRepository;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import com.SkillSwap.SkillSwapMain.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Notification sendNotification(Long userId, String message) {
        User user = userRepository.findById(userId).orElseThrow();
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setIsRead(false);

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications/" + userId, message);
        return notification;
    }

    @Override
    public List<Notification> getNotifications(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return notificationRepository.findByUser(user);
    }
}