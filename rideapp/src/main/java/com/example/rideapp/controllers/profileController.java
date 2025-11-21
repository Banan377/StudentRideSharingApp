package com.example.rideapp.controllers;

import com.example.rideapp.models.UserModel;
import com.example.rideapp.services.ProfileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
public class profileController {

    @Autowired
    private ProfileService profileService;

    // جلب بيانات المستخدم
    @GetMapping("/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        UserModel user = profileService.getUserProfile(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("المستخدم غير موجود");
        }

        return ResponseEntity.ok(user);
    }

    // تحديث بيانات المستخدم
    @PutMapping("/update")
    public ResponseEntity<?> updateProfile(@RequestBody UserModel updatedUser) {

        boolean success = profileService.updateProfile(
                updatedUser.getEmail(),
                updatedUser.getCollege(),
                updatedUser.getEmergencyContacts(),
                updatedUser.getPassword()
        );

        if (!success) {
            return ResponseEntity.badRequest().body("فشل تحديث البيانات");
        }

        return ResponseEntity.ok("تم تحديث بيانات الحساب بنجاح");
    }
}
