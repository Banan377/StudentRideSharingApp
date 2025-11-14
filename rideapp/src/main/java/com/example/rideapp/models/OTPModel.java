package com.example.rideapp.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class OTPModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpID;

    private String email;
    private String otpCode;
    private LocalDateTime expirationTime;

    public OTPModel() {}

    public OTPModel(String email, String otpCode, LocalDateTime expirationTime) {
        this.email = email;
        this.otpCode = otpCode;
        this.expirationTime = expirationTime;
    }

    public Long getOtpID() { return otpID; }
    public void setOtpID(Long otpID) { this.otpID = otpID; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public LocalDateTime getExpirationTime() { return expirationTime; }
    public void setExpirationTime(LocalDateTime expirationTime) { this.expirationTime = expirationTime; }
}
