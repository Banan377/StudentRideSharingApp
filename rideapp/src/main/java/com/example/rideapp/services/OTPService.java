package com.example.rideapp.services;

import com.example.rideapp.models.OTPModel;
import com.example.rideapp.repositories.OTPRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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

    private static final int OTP_EXPIRATION_MINUTES = 5;

    @Transactional
    public void deleteOTP(OTPModel otpModel) {
        otpRepository.delete(otpModel);
    }

    public OTPModel generateAndSendOTP(String email) {
       

        // إنشاء OTP عشوائي 6 أرقام
        String otp = String.format("%06d", new Random().nextInt(999999));

        OTPModel otpModel = new OTPModel(email, otp, LocalDateTime.now().plusMinutes(OTP_EXPIRATION_MINUTES));
        otpRepository.save(otpModel);

        sendOTPEmail(email, otp);
        System.out.println("Generated OTP for " + email + ": " + otp);

        return otpModel;
    }

    public boolean verifyOTP(String email, String otpCode) {
        Optional<OTPModel> existingOTP = otpRepository.findByEmail(email);

        if (existingOTP.isPresent()) {
            OTPModel otpModel = existingOTP.get();

            if (otpModel.getOtpCode().equals(otpCode) &&
                otpModel.getExpirationTime().isAfter(LocalDateTime.now())) {

                // حذف OTP بعد التحقق
                 deleteOTP(otpModel);
                return true;
            }
        }
        return false;
    }

    private void sendOTPEmail(String email, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("رمز التحقق OTP");
            message.setText("كود التحقق الخاص بك هو: " + otp +
                    "\nصالح لمدة " + OTP_EXPIRATION_MINUTES + " دقائق.");
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}
