package com.SkillSwap.SkillSwapMain.repositories;

import com.SkillSwap.SkillSwapMain.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface OtpRepository extends JpaRepository<Otp,Long> {
    Optional<Otp> findByMobileNumberAndOtp(String mobileNumber, String otp);
}
