package com.SkillSwap.SkillSwapMain.repositories;

import com.SkillSwap.SkillSwapMain.entity.RefreshToken;
import com.SkillSwap.SkillSwapMain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUser(User user);
    Optional<RefreshToken> findByUserId(Long userId);
    void deleteByUser(User user);
}
