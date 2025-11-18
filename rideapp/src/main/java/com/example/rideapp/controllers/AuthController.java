package com.example.rideapp.controllers;

import com.example.rideapp.models.Student;
import com.example.rideapp.models.UserModel;

import com.example.rideapp.repositories.StudentRepository;
import com.example.rideapp.services.AuthService;
import com.example.rideapp.services.OTPService;
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

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/send-otp")
    public ResponseEntity<?> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<Student> student = studentRepository.findById(email);
        if (student.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "الإيميل غير مسجل في النظام"));
        }

        if (authService.isUserRegistered(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "هذا الإيميل مسجل مسبقًا!"));
        }

        // إرسال OTP
        otpService.generateAndSendOTP(email);
        return ResponseEntity.ok(Map.of("message", "تم إرسال كود التحقق"));
    }

    // 2) تحقق من OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otpCode = request.get("otpCode");

        boolean isValid = otpService.verifyOTP(email, otpCode);

        if (isValid) {
            return ResponseEntity.ok(Map.of("message", "تم التحقق بنجاح", "success", true));
        } else {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "كود OTP غير صحيح أو منتهي الصلاحية", "success", false));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> requestData) {
        try {
            // استخراج بيانات المستخدم مع التحقق من null
            final UserModel user = new UserModel();
            user.setEmail((String) requestData.get("email"));
            user.setName((String) requestData.get("name"));
            user.setPassword((String) requestData.get("password"));
            user.setCollege((String) requestData.get("college"));
            user.setGender((String) requestData.get("gender"));
            user.setEmergencyContacts((String) requestData.get("emergencyContacts"));
            user.setRateAverage(0);
            user.setStatus("active");

           
            user.setRole("passenger");

            // استخراج بيانات السائق مع التحقق
            Map<String, Object> driverData = null;
            if (requestData.containsKey("driverData") && requestData.get("driverData") != null) {
                driverData = (Map<String, Object>) requestData.get("driverData");
                user.setRole("driver"); 
            }

            // التحقق من البيانات الأساسية
            if (user.getEmail() == null || user.getPassword() == null) {
                return ResponseEntity.badRequest().body("البريد الإلكتروني وكلمة المرور مطلوبان");
            }

            // منع تسجيل مستخدم موجود مسبقًا
            if (authService.isUserRegistered(user.getEmail())) {
                return ResponseEntity.badRequest().body("هذا المستخدم مسجّل مسبقًا");
            }

            // إنشاء الحساب
            authService.createAccount(user, driverData);

            String message = "تم إنشاء الحساب بنجاح: " + user.getEmail();
            if (driverData != null && !driverData.isEmpty()) {
                message += " (تم التسجيل كراكب وسائق - بانتظار موافقة الإدارة)";
            } else {
                message += " (تم التسجيل كراكب)";
            }

            return ResponseEntity.ok(message);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("خطأ في إنشاء الحساب: " + e.getMessage());
        }
    }
}
