package com.SkillSwap.SkillSwapMain.repositories;
import com.SkillSwap.SkillSwapMain.entity.Notification;
import com.SkillSwap.SkillSwapMain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByUser(User user);
}