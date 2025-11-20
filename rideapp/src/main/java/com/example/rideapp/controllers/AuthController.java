package com.example.rideapp.controllers;

import com.example.rideapp.models.Student;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.services.AuthService;
import com.example.rideapp.services.OTPService;
import com.example.rideapp.repositories.StudentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

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
        boolean isResetPassword = Boolean.parseBoolean(request.getOrDefault("resetPasswordMode", "false"));

        // 1) تحقق أن الإيميل تابع للطلاب
        Optional<Student> student = studentRepository.findById(email);
        if (student.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "الإيميل غير مسجل في النظام"));
        }

        // 2) تسجيل جديد → يمنع الإيميل المتكرر
        if (!isResetPassword && authService.isUserRegistered(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "هذا الإيميل مسجل مسبقًا!"));
        }

        // 3) نسيان كلمة المرور → يجب أن يكون الإيميل مسجّل فعلاً
        if (isResetPassword && !authService.isUserRegistered(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "لا يوجد حساب مرتبط بهذا البريد"));
        }

        try {
            otpService.generateAndSendOTP(email);
            return ResponseEntity.ok(Map.of("message", "تم إرسال كود التحقق"));
        } catch (IllegalStateException ex) {

            if (ex.getMessage().equals("OTP_NOT_EXPIRED")) {
                return ResponseEntity.badRequest()
                        .body(Map.of("message", "تم إرسال كود مسبقًا — الرجاء الانتظار حتى انتهاء الوقت"));
            }

            return ResponseEntity.badRequest().body(Map.of("message", "حدث خطأ أثناء إرسال الكود"));
        }
    }


    // ===========================================================
    //                  التحقق من كود OTP
    // ===========================================================
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpCode = request.get("otpCode");

        boolean isValid = otpService.verifyOTP(email, otpCode);

        if (isValid) {
            return ResponseEntity.ok(Map.of("message", "تم التحقق بنجاح", "success", true));
        }

        return ResponseEntity.badRequest()
                .body(Map.of("message", "كود OTP غير صحيح أو منتهي الصلاحية", "success", false));
    }


    // ===========================================================
    //                 إنشاء حساب جديد (تسجيل)
    // ===========================================================
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


    // ===========================================================
    //             إعادة تعيين كلمة المرور
    // ===========================================================
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
