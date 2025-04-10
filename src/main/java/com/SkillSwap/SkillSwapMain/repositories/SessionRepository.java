package com.SkillSwap.SkillSwapMain.repositories;
import com.SkillSwap.SkillSwapMain.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
}