package com.SkillSwap.SkillSwapMain.controllers;

import com.SkillSwap.SkillSwapMain.dto.request.LoginRequest;
import com.SkillSwap.SkillSwapMain.dto.request.RegisterRequest;
import com.SkillSwap.SkillSwapMain.errorHandling.BaseResponse;
import com.SkillSwap.SkillSwapMain.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        BaseResponse<?> response = authService.registerUser(request);
        return ResponseEntity.status(response.resultCode()).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request) {
        if (request.mobileNumber() != null &&
                request.email() == null &&
                request.password() == null) {
            // Mobile number login
            BaseResponse<?> response = authService.initiateMobileLogin(request.mobileNumber());
            return ResponseEntity.status(response.resultCode()).body(response);
        } else if (request.email() != null && request.password() != null) {
            // Email/password login
            BaseResponse<?> response = authService.loginWithEmail(request);
            return ResponseEntity.status(response.resultCode()).body(response);
        } else {
            return ResponseEntity.badRequest().body(
                    new BaseResponse<>(
                            400,
                            "Invalid login request: Provide either email/password or mobileNumber"
                    )
            );
        }
    }

    @PostMapping("/validate-otp")
    public ResponseEntity<?> validateOtp(@RequestParam String mobileNumber, @RequestParam String otp) {
        String updatedMobileNumber= "+"+mobileNumber;
        BaseResponse<?> response = authService.validateOtp(updatedMobileNumber, otp);
        return ResponseEntity.status(response.resultCode()).body(response);
    }

    @GetMapping("/test")
    public String testC() {
        return "Running Success!!";
    }
}