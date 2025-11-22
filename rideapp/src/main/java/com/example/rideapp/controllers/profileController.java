package com.example.rideapp.controllers;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.DriverRepository;
import com.example.rideapp.services.ProfileService;
import com.example.rideapp.services.DriverService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/profile")
public class profileController {

    @Autowired
    private ProfileService profileService;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DriverService driverService;

    @GetMapping("/{email}")
    public ResponseEntity<?> getProfile(@PathVariable String email) {
        UserModel user = profileService.getUserProfile(email);

        if (user == null) {
            return ResponseEntity.badRequest().body("المستخدم غير موجود");
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/driver/{email}")
    public ResponseEntity<?> getDriverProfile(@PathVariable String email) {
        try {
            DriverModel driver = driverService.getDriver(email);

            if (driver == null) {
                return ResponseEntity.ok(Map.of(
                        "isDriver", false,
                        "message", "لا توجد بيانات سائق"));
            }

            return ResponseEntity.ok(Map.of(
                    "isDriver", true,
                    "driverData", driver));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "حدث خطأ: " + e.getMessage()));
        }
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<?> updateProfile(
            @PathVariable String email,
            @RequestBody Map<String, Object> updatedUser) {

        boolean success = profileService.updateProfile(
                email,
                (String) updatedUser.get("college"),
                (String) updatedUser.get("emergencyContacts"),
                (String) updatedUser.get("password"));

        if (!success) {
            return ResponseEntity.badRequest().body("فشل تحديث البيانات");
        }

        return ResponseEntity.ok("تم تحديث بيانات الحساب بنجاح");
    }

    @PutMapping("/driver/update/{email}")
    public ResponseEntity<?> updateDriverProfile(
            @PathVariable String email,
            @RequestBody DriverUpdateRequest updateRequest) {
        try {
            DriverModel existingDriver = driverService.getDriver(email);

            if (existingDriver == null) {
                return ResponseEntity.badRequest().body("لا توجد بيانات سائق للتحديث");
            }

            // تحديث البيانات باستخدام الـ Service الموجود
            DriverModel updatedDriver = new DriverModel();
            updatedDriver.setEmail(email);
            updatedDriver.setCarModel(updateRequest.getCarModel());
            updatedDriver.setCarColor(updateRequest.getCarColor());
            updatedDriver.setSeatsAvailable(updateRequest.getSeatsAvailable());
            updatedDriver.setLicenseNumber(updateRequest.getLicenseNumber());

            boolean success = driverService.updateDriver(updatedDriver);

            if (!success) {
                return ResponseEntity.badRequest().body("فشل في تحديث البيانات");
            }

            return ResponseEntity.ok("تم تحديث بيانات السائق بنجاح");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("فشل في تحديث البيانات: " + e.getMessage());
        }
    }

    @GetMapping("/role-status/{email}")
    public ResponseEntity<?> getRoleStatus(@PathVariable String email) {

        boolean exists = driverRepository.existsByEmail(email);

        if (!exists) {
            return ResponseEntity.ok(Map.of(
                    "status", "noDriverRecord"));
        }

        DriverModel driver = driverRepository.findByEmail(email);

        if (driver.getStatus().equals("approved")) {
            return ResponseEntity.ok(Map.of(
                    "status", "driverVerified"));
        }

        if (driver.getStatus().equals("pending")) {
            return ResponseEntity.ok(Map.of(
                    "status", "pending"));
        }

        return ResponseEntity.ok(Map.of(
                "status", "unknown"));
    }

    public static class DriverUpdateRequest {
        private String licenseNumber;
        private String carModel;
        private String carColor;
        private int seatsAvailable;

        // Getters and Setters
        public String getLicenseNumber() {
            return licenseNumber;
        }

        public void setLicenseNumber(String licenseNumber) {
            this.licenseNumber = licenseNumber;
        }

        public String getCarModel() {
            return carModel;
        }

        public void setCarModel(String carModel) {
            this.carModel = carModel;
        }

        public String getCarColor() {
            return carColor;
        }

        public void setCarColor(String carColor) {
            this.carColor = carColor;
        }

        public int getSeatsAvailable() {
            return seatsAvailable;
        }

        public void setSeatsAvailable(int seatsAvailable) {
            this.seatsAvailable = seatsAvailable;
        }
    }
}
