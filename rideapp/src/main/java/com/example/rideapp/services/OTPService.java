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

    private static final int OTP_EXP_MINUTES = 2;

    @Transactional
    public void deleteOTP(OTPModel otpModel) {
        otpRepository.delete(otpModel);
    }


  
    public OTPModel generateAndSendOTP(String email) {

        Optional<OTPModel> existing = otpRepository.findByEmail(email);

        if (existing.isPresent()) {
            OTPModel oldOtp = existing.get();

            if (oldOtp.getExpirationTime().isAfter(LocalDateTime.now())) {
                throw new IllegalStateException("OTP_NOT_EXPIRED");
            }

            otpRepository.delete(oldOtp);
        }

        String otp = String.format("%06d", new Random().nextInt(999999));

        OTPModel otpModel =
                new OTPModel(email, otp, LocalDateTime.now().plusMinutes(OTP_EXP_MINUTES));

        otpRepository.save(otpModel);

        sendOTPEmail(email, otp);

        System.out.println("Generated OTP for " + email + ": " + otp);

        return otpModel;
    }



    public boolean verifyOTP(String email, String otpCode) {

        Optional<OTPModel> existing = otpRepository.findByEmail(email);

        if (existing.isEmpty()) return false;

        OTPModel otpModel = existing.get();

        if (otpModel.getExpirationTime().isBefore(LocalDateTime.now()))
            return false;

        boolean valid = otpModel.getOtpCode().equals(otpCode);

        if (valid) deleteOTP(otpModel);

        return valid;
    }




    private void sendOTPEmail(String email, String otp) {
        try {
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
}
