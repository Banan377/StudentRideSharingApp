package com.example.rideapp.controllers;

import com.example.rideapp.models.OTPModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.services.AuthService;
import com.example.rideapp.services.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OTPService otpService;

   
    @PostMapping("/register")
    public String register(@RequestBody UserModel user) {
        authService.createAccount(user);

        OTPModel otp = otpService.generateAndSendOTP(user.getEmail());

        return "Account created. OTP sent to " + user.getEmail();
    }

    
    @PostMapping("/login")
    public boolean login(@RequestBody UserModel user) {
        return authService.login(user.getEmail(), user.getPassword());
    }

    
    @PostMapping("/send-otp")
    public String sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        otpService.generateAndSendOTP(email);
        return "OTP sent successfully";
    }

    
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpCode = request.get("otpCode");

        boolean isValid = otpService.verifyOTP(email, otpCode);

        if (isValid) {
            return "OTP verified successfully!";
        } else {
            return "Invalid or expired OTP.";
        }
    }
}
