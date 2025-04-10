package com.SkillSwap.SkillSwapMain.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessions")
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", nullable = false)
    private Match match;

    @Column(nullable = false)
    private LocalDateTime scheduledTime;

    @Column(nullable = false)
    private Integer duration; // In minutes

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionStatus status;

    private String feedbackUser1;
    private String feedbackUser2;

    private Integer ratingUser1;
    private Integer ratingUser2;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public enum SessionStatus {
        SCHEDULED, COMPLETED, CANCELLED
    }
}
