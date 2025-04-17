package com.SkillSwap.SkillSwapMain.dto.request;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MatchDTO {

    private Long id;

    private Long user1Id;

    private Long user2Id;

    private String status;

    private LocalDateTime createdAt;
}
