package com.SkillSwap.SkillSwapMain.services;

import com.SkillSwap.SkillSwapMain.config.JwtUtil;
import com.SkillSwap.SkillSwapMain.config.SessionIdGenerator;
import com.SkillSwap.SkillSwapMain.dto.request.LoginRequest;
import com.SkillSwap.SkillSwapMain.dto.request.RegisterRequest;
import com.SkillSwap.SkillSwapMain.entity.Otp;
import com.SkillSwap.SkillSwapMain.entity.RefreshToken;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.errorHandling.BaseResponse;
import com.SkillSwap.SkillSwapMain.mapper.UserMapper;
import com.SkillSwap.SkillSwapMain.otp.SmsService;
import com.SkillSwap.SkillSwapMain.repositories.OtpRepository;
import com.SkillSwap.SkillSwapMain.repositories.RefreshTokenRepository;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private SmsService smsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Register a new user
    public BaseResponse<?> registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Email Already Exists!!"
            );
        }
        if (userRepository.findByMobileNumber(request.mobileNumber()).isPresent()) {
            return new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Mobile Number Already Exists!!"
            );
        }

        User user = userMapper.toEntity(request);
        userRepository.save(user);
        return new BaseResponse<>(
                HttpStatus.CREATED.value(),
                "User Registered Successfully!!"
        );
    }

    // Login with email and password
    public BaseResponse<?> loginWithEmail(LoginRequest request) {
        User user = userRepository.findByEmail(request.email()).orElse(null);
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Login Failed (Invalid Credentials)!!"
            );
        }

        // Generate IP address
        String ipAddress = getLocalIpAddress();

        // Generate session ID
        String sessionId = SessionIdGenerator.generateSessionId(user, ipAddress);

        // Generate tokens
        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // Remove old refresh token
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        // Save new refresh token
        RefreshToken refreshTokenEntity = new RefreshToken(
                null,
                user,
                refreshToken,
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        refreshTokenRepository.save(refreshTokenEntity);

        // Update user with session ID and token
        user.setSessionId(sessionId);
        user.setJwtToken(accessToken);
        userRepository.save(user);

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("sessionId", sessionId);
        return new BaseResponse<>(
                HttpStatus.OK.value(),
                "Login Successful",
                response
        );
    }

    // Initiate login with mobile number (send OTP)
    public BaseResponse<?> initiateMobileLogin(String mobileNumber) {
        User user = userRepository.findByMobileNumber(mobileNumber).orElse(null);
        if (user == null) {
            return new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Mobile Number Not Registered!!"
            );
        }

        // Generate 5-digit OTP
        String otp = generateOtp();

        // Save OTP
        Otp otpEntity = new Otp();
        otpEntity.setMobileNumber(mobileNumber);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(10));
        otpRepository.save(otpEntity);

        // Send OTP via SMS
        smsService.sendOtp(mobileNumber, otp);

        return new BaseResponse<>(
                HttpStatus.OK.value(),
                "A 5-digit OTP has been sent to your registered mobile number"
        );
    }

    // Validate OTP for login
    public BaseResponse<?> validateOtp(String mobileNumber, String otp) {

        Optional<Otp> otpEntity = otpRepository.findByMobileNumberAndOtp(mobileNumber, otp);
        if (otpEntity.isEmpty()) {
            return new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Invalid OTP"
            );
        }

        Otp storedOtp = otpEntity.get();
        if (LocalDateTime.now().isAfter(storedOtp.getExpiresAt())) {
            otpRepository.delete(storedOtp); // Clean up expired OTP
            return new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "OTP has expired"
            );
        }

        // OTP is valid, proceed with login
        User user = userRepository.findByMobileNumber(mobileNumber).orElseThrow();

        // Generate IP address
        String ipAddress = getLocalIpAddress();

        // Generate session ID
        String sessionId = SessionIdGenerator.generateSessionId(user, ipAddress);

        // Generate tokens
        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // Remove old refresh token
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        // Save new refresh token
        RefreshToken refreshTokenEntity = new RefreshToken(
                null,
                user,
                refreshToken,
                Instant.now().plus(7, ChronoUnit.DAYS)
        );
        refreshTokenRepository.save(refreshTokenEntity);

        // Update user
        user.setSessionId(sessionId);
        user.setJwtToken(accessToken);
        userRepository.save(user);

        // Clean up OTP
        otpRepository.delete(storedOtp);

        // Prepare response
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("sessionId", sessionId);
        return new BaseResponse<>(
                HttpStatus.OK.value(),
                "Login Successful",
                response
        );
    }

    // Utility to get local IP address
    private String getLocalIpAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            return "Unable to get IP";
        }
    }

    // Generate 5-digit OTP
    private String generateOtp() {
        SecureRandom random = new SecureRandom();
        int otp = 10000 + random.nextInt(90000); // 10000 to 99999
        return String.valueOf(otp);
    }
}
