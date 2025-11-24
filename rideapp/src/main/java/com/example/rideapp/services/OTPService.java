package com.example.rideapp.services;

import com.example.rideapp.models.OTPModel;
import com.example.rideapp.repositories.OTPRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {

    @Autowired
    private OTPRepository otpRepository;

    @Autowired
    private JavaMailSender mailSender;

    private static final int OTP_EXP_MINUTES = 2;

    @Transactional
    public void deleteOTP(OTPModel otpModel) {
        otpRepository.delete(otpModel);
    }

    public OTPModel generateAndSendOTP(String email, String otpType) {

        Optional<OTPModel> existing = otpRepository.findByEmail(email);

        if (existing.isPresent()) {
            OTPModel oldOtp = existing.get();

            if (oldOtp.getExpirationTime().isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("OTP_NOT_EXPIRED");
            }

            otpRepository.delete(oldOtp);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        OTPModel otpModel = new OTPModel(
                email,
                otp,
                otpType,
                LocalDateTime.now().plusMinutes(2));

        otpRepository.save(otpModel);

        System.out.println(" OTP Generated for " + email + ": " + otp);

        sendOTPEmail(email, otp);

        return otpModel;
    }

    @Async

    private void sendOTPEmail(String email, String otp) {
        try {
            System.out.println("OTP GENERATED = " + otp);
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(email);
            msg.setSubject("رمز التحقق");
            msg.setText("رمز التحقق الخاص بك هو: " + otp
                    + "\nصالح لمدة " + OTP_EXP_MINUTES + " دقائق.");

            mailSender.send(msg);

        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }

    public OTPModel getOTPByEmail(String email) {
        return otpRepository.findByEmail(email).orElse(null);
    }

}