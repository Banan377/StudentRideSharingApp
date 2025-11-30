package com.example.rideapp.controllers;

import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData) {
        try {
            String email = loginData.get("email");
            String password = loginData.get("password");

            System.out.println("محاولة تسجيل دخول: " + email);

            if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "البريد الإلكتروني وكلمة المرور مطلوبان"));
            }

            if (!email.endsWith("@sm.imamu.edu.sa")) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "يجب أن يكون البريد الإلكتروني من نطاق جامعة الإمام محمد بن سعود الإسلامية"));
            }

            Optional<UserModel> userOptional = userRepository.findById(email);

            if (userOptional.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "الإيميل غير مسجل في التطبيق"));
            }

            UserModel user = userOptional.get();

            // التحقق من حالة الحساب
            if ("inactive".equals(user.getStatus()) || "suspended".equals(user.getStatus())) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "الحساب غير نشط أو موقوف. يرجى التواصل مع الإدارة"));
            }

            // تحقق من كلمة المرور
            boolean passwordMatches = passwordEncoder.matches(password, user.getPassword());

            if (!passwordMatches) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "كلمة المرور خاطئة"));
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "تم تسجيل الدخول بنجاح");

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", user.getEmail());
            userInfo.put("name", user.getName() != null ? user.getName() : "مستخدم");
            userInfo.put("role", user.getRole() != null ? user.getRole() : "passenger");
            userInfo.put("college", user.getCollege() != null ? user.getCollege() : "غير محدد");
            userInfo.put("gender", user.getGender() != null ? user.getGender() : "غير محدد");
            userInfo.put("status", user.getStatus() != null ? user.getStatus() : "active");
            userInfo.put("emergencyContacts", user.getEmergencyContacts() != null ? user.getEmergencyContacts() : "");

            response.put("user", userInfo);

            String redirectUrl;
            if ("admin".equals(user.getRole())) {
                redirectUrl = "adminDashboard.html";

            } else if ("driver".equals(user.getRole()) && "approved".equals(user.getStatus())) {
                redirectUrl = "driverUI.html";

            } else {
                redirectUrl = "passengerRides.html";
            }

            response.put("redirectUrl", redirectUrl);
            return ResponseEntity.ok(response);

           

        } catch (Exception e) {
            System.err.println("خطأ في تسجيل الدخول: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(Map.of(
                    "success", false,
                    "message", "حدث خطأ غير متوقع أثناء تسجيل الدخول"));
        }
    }

    @GetMapping("/check-session")
    public ResponseEntity<?> checkSession(@RequestHeader(value = "User-Email", required = false) String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "authenticated", false,
                        "message", "لا توجد جلسة نشطة"));
            }

            Optional<UserModel> userOptional = userRepository.findById(email);
            if (userOptional.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "authenticated", false,
                        "message", "المستخدم غير موجود"));
            }

            UserModel user = userOptional.get();
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("email", user.getEmail());
            userInfo.put("name", user.getName());
            userInfo.put("role", user.getRole());
            userInfo.put("college", user.getCollege());
            userInfo.put("status", user.getStatus());

            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", userInfo));

        } catch (Exception e) {
            System.err.println("خطأ في التحقق من الجلسة: " + e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of(
                    "authenticated", false,
                    "message", "خطأ في التحقق من الجلسة"));
        }
    }

}