package com.example.rideapp.controllers;

import com.example.rideapp.models.OTPModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.services.AuthService;
import com.example.rideapp.services.OTPService;
import com.example.rideapp.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private OTPService otpService;

    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpType = request.get("otpType");
        boolean isResetPassword = Boolean.parseBoolean(request.getOrDefault("resetPasswordMode", "false"));

        // التحقق من وجود الإيميل في قاعدة الطلاب
        if (!studentRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "الإيميل غير مسجل في قاعدة بيانات الطلاب"));
        }

        // تسجيل جديد
        if (!isResetPassword && authService.isUserRegistered(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "هذا الإيميل مسجل مسبقًا!"));
        }

        // استعادة كلمة المرور
        if (isResetPassword && !authService.isUserRegistered(email)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "لا يوجد حساب مرتبط بهذا البريد"));
        }

        try {
            otpService.generateAndSendOTP(email, otpType);
            return ResponseEntity.ok(Map.of("message", "تم إرسال كود التحقق"));
        } catch (Exception ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "حدث خطأ أثناء إرسال الكود"));
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {

        String email = request.get("email");
        String otpCode = request.get("otpCode");

        OTPModel otp = otpService.getOTPByEmail(email);

        if (otp == null) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "لا يوجد كود تحقق مخصص لهذا البريد"));
        }

        // 2) التحقق من انتهاء الصلاحية
        if (otp.getExpirationTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "انتهت صلاحية الكود"));
        }

        // 3) التحقق أن الكود صحيح
        if (!otp.getOtpCode().equals(otpCode)) {
            return ResponseEntity.badRequest()
                    .body(Map.of("success", false, "message", "كود OTP غير صحيح"));
        }

        // 4) حذف الكود بعد الاستخدام
        otpService.deleteOTP(otp);

        // 5) التوجيه حسب نوع الـ OTP
        switch (otp.getOtpType()) {

            case "password_reset":
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "redirect", "forgotPassword.html",
                        "message", "تم التحقق، يمكنك إعادة تعيين كلمة المرور"));

            case "signup":
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "redirect", "signup-form.html",
                        "message", "تم التحقق، أكمل إنشاء حسابك"));

            case "change_from_settings":
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "redirect", "resetPassword.html",
                        "message", "تم التحقق، يمكنك تغيير كلمة المرور"));
        }

        return ResponseEntity.ok(Map.of(
                "success", true,
                "message", "تم التحقق بنجاح"));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestData) {
        try {
            UserModel user = new UserModel();
            user.setEmail((String) requestData.get("email"));
            user.setName((String) requestData.get("name"));
            user.setPassword((String) requestData.get("password"));
            user.setCollege((String) requestData.get("college"));
            user.setGender((String) requestData.get("gender"));
            user.setEmergencyContacts((String) requestData.get("emergencyContacts"));
            user.setRateAverage(0);
            user.setStatus("active");
            user.setRole("passenger");

            Map<String, Object> driverData = null;
            if (requestData.containsKey("driverData") && requestData.get("driverData") != null) {
                driverData = (Map<String, Object>) requestData.get("driverData");
                user.setRole("driver");
            }

            if (user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("البريد الإلكتروني وكلمة المرور مطلوبان");
            }

            if (authService.isUserRegistered(user.getEmail())) {
                return ResponseEntity.badRequest().body("هذا المستخدم مسجّل مسبقًا");
            }

            authService.createAccount(user, driverData);

            String message = "تم إنشاء الحساب بنجاح: " + user.getEmail();
            if (driverData != null) {
                message += " (تم التسجيل كراكب وسائق - بانتظار موافقة الإدارة)";
            } else {
                message += " (تم التسجيل كراكب)";
            }

            return ResponseEntity.ok(message);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("خطأ في إنشاء الحساب: " + e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        try {
            authService.updatePassword(email, newPassword);
            return ResponseEntity.ok(Map.of("message", "تم تغيير كلمة المرور بنجاح"));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "حدث خطأ أثناء تغيير كلمة المرور"));
        }
    }
}
