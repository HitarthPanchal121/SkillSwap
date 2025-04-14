package com.SkillSwap.SkillSwapMain.repositories;

import com.SkillSwap.SkillSwapMain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findBySessionIdAndJwtToken(String sessionId, String jwtToken);

    Optional<User> findByMobileNumber(String mobileNumber);
}