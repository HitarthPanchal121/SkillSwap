package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.entity.Notification;
import com.SkillSwap.SkillSwapMain.errorHandling.BaseResponse;
import com.SkillSwap.SkillSwapMain.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{userId}")
    public ResponseEntity<BaseResponse<?>> getNotifications(@PathVariable Long userId) {
        List<Notification> notifications = notificationService.getNotifications(userId);
        BaseResponse<List<Notification>> response = new BaseResponse<>(200, "Notifications fetched successfully", notifications);
        return ResponseEntity.status(response.resultCode()).body(response);
    }
}