package com.SkillSwap.SkillSwapMain.dto.request;


import com.SkillSwap.SkillSwapMain.entity.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationDTO {

    private Long id;

    private User userid;

    private String message;

    private boolean isRead;

    private LocalDateTime createdAt;
}