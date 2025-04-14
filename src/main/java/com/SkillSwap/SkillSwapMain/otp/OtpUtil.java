package com.SkillSwap.SkillSwapMain.otp;

import java.security.SecureRandom;

public class OtpUtil {
    public static String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 10000 + random.nextInt(90000); // Generates 10000 to 99999
        return String.valueOf(otp);
    }
}