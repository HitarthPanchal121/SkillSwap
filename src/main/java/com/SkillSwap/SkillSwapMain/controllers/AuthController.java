package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.config.JwtUtil;
import com.SkillSwap.SkillSwapMain.config.SessionIdGenerator;
import com.SkillSwap.SkillSwapMain.dto.request.LoginRequest;
import com.SkillSwap.SkillSwapMain.dto.request.RegisterRequest;
import com.SkillSwap.SkillSwapMain.entity.RefreshToken;
import com.SkillSwap.SkillSwapMain.entity.User;
import com.SkillSwap.SkillSwapMain.errorHandling.BaseResponse;
import com.SkillSwap.SkillSwapMain.mapper.UserMapper;
import com.SkillSwap.SkillSwapMain.repositories.RefreshTokenRepository;
import com.SkillSwap.SkillSwapMain.repositories.UserRepository;
import com.SkillSwap.SkillSwapMain.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private UserMapper userMapper;
    @Autowired
    public AuthController (UserMapper userMapper)
    {
        this.userMapper=userMapper;
    }



    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Email Already Exists!!"
                    )
            );

        }


        User user = userMapper.toEntity(request);


        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new BaseResponse<>(
                HttpStatus.CREATED.value(),
                "User Registered Successfully!!"
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        User user = userRepository.findByEmail(request.email()).orElse(null);
        System.out.println(user);
        if (user == null || !passwordEncoder.matches(request.password(), user.getPassword())) {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(
                            HttpStatus.BAD_REQUEST.value(),
                            "Login Failed ( Invalid Credentials ) !!"
                    )
            );

        }

        // ✅ Extract Client IP
        String ipAddress = getClientIpAddress(httpRequest);

        // ✅ Generate Session ID
        String sessionId = SessionIdGenerator.generateSessionId(user, ipAddress);

        // ✅ Generate Tokens
        String accessToken = jwtUtil.generateToken(user.getEmail());
        String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());

        // ✅ Remove old refresh token (if exists)
        refreshTokenRepository.findByUser(user).ifPresent(refreshTokenRepository::delete);

        // ✅ Save new refresh token
        RefreshToken refreshTokenEntity = new RefreshToken(
                null,
                user,
                refreshToken,
                Instant.now().plus(7, ChronoUnit.DAYS) // 7 days expiry
        );
        refreshTokenRepository.save(refreshTokenEntity);

        // ✅ Store session ID and Access Token in User
        user.setSessionId(sessionId);
        user.setJwtToken(accessToken);
        userRepository.save(user);

        // ✅ Response with Access Token, Refresh Token & Session ID
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("sessionId", sessionId);

        return ResponseEntity.ok(response);
    }

    // ✅ Extract client IP (Handles proxies)
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.split(",")[0]; // If multiple IPs, take the first
    }

    @GetMapping("/Test")
    public String testC() {
        return "Running Success!!";
    }
}
