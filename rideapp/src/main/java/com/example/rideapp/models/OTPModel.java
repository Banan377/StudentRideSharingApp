package com.example.rideapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp")
public class OTPModel {

    @Id
    private String email;      
    private String otpCode;
    private LocalDateTime expirationTime;

    public OTPModel() {}

    public OTPModel(String email, String otpCode, LocalDateTime expirationTime) {
        this.email = email;
        this.otpCode = otpCode;
        this.expirationTime = expirationTime;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }

    public LocalDateTime getExpirationTime() { return expirationTime; }
    public void setExpirationTime(LocalDateTime expirationTime) { this.expirationTime = expirationTime; }
}
