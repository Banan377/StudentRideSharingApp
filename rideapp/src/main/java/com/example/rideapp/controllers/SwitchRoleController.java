package com.example.rideapp.controllers;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.DriverRepository;
import com.example.rideapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/switch")
public class SwitchRoleController {

    @Autowired
    private DriverRepository driverRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/checkDriverStatus")
    public Map<String, Object> checkDriverStatus(@RequestParam String email) {
        try {
            DriverModel driver = driverRepo.findByEmail(email);

            if (driver != null) {
                switch (driver.getStatus()) {
                    case "approved":
                        return Map.of("status", "verified_driver", "message", "سائق موثق");
                    case "pending":
                        return Map.of("status", "pending_driver", "message", "طلب قيد المراجعة");
                    case "rejected":
                        return Map.of("status", "rejected_driver", "message", "تم رفض الطلب");
                }
            }

            return Map.of("status", "not_driver", "message", "المستخدم ليس سائقاً");

        } catch (Exception e) {
            return Map.of("status", "error", "message", "حدث خطأ: " + e.getMessage());
        }
    }

    @GetMapping("/verifyDriver")
    public boolean verifyDriver(@RequestParam String email) {
        try {
            UserModel user = userRepo.findById(email).orElse(null);
            if (user == null)
                return false;

            return "driver".equals(user.getRole());
        } catch (Exception e) {
            return false;
        }
    }

    // تسجيل سائق جديد
    @PostMapping("/registerDriver")
    public Map<String, Object> registerDriver(@RequestBody DriverRegistrationRequest driverData) {
        try {
            // التحقق إذا كان المستخدم مسجل مسبقاً كسائق
            DriverModel existingDriver = driverRepo.findByEmail(driverData.getEmail());

            if (existingDriver != null) {
                return Map.of(
                        "success", false,
                        "message", "المستخدم مسجل مسبقاً كسائق");
            }

            // إنشاء سجل سائق جديد
            DriverModel newDriver = new DriverModel();
            newDriver.setEmail(driverData.getEmail());
            newDriver.setLicenseNumber(driverData.getLicenseNumber());
            newDriver.setCarModel(driverData.getCarModel());
            newDriver.setCarColor(driverData.getCarColor());
            newDriver.setCarPlate(driverData.getPlateNumber());
            newDriver.setSeatsAvailable(driverData.getAvailableSeats());
            newDriver.setLicenseImage(driverData.getLicenseFile());
            newDriver.setStatus("pending");

            DriverModel savedDriver = driverRepo.save(newDriver);

            // تحديث حالة المستخدم إلى "driver"
            UserModel user = userRepo.findById(driverData.getEmail()).orElse(null);
            if (user != null) {
                user.setRole("driver");
                userRepo.save(user);
            }

            return Map.of(
                    "success", true,
                    "message", "تم التسجيل بنجاح",
                    "driver", savedDriver);

        } catch (Exception e) {
            return Map.of(
                    "success", false,
                    "message", "حدث خطأ في التسجيل: " + e.getMessage());
        }
    }

    // كلاس طلب تسجيل السائق
    public static class DriverRegistrationRequest {
        private String email;
        private String licenseNumber;
        private String carModel;
        private String carColor;
        private String plateNumber;
        private int availableSeats;
        private String licenseFile;

        // Getters and Setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

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

        public String getPlateNumber() {
            return plateNumber;
        }

        public void setPlateNumber(String plateNumber) {
            this.plateNumber = plateNumber;
        }

        public int getAvailableSeats() {
            return availableSeats;
        }

        public void setAvailableSeats(int availableSeats) {
            this.availableSeats = availableSeats;
        }

        public String getLicenseFile() {
            return licenseFile;
        }

        public void setLicenseFile(String licenseFile) {
            this.licenseFile = licenseFile;
        }
    }

    // كلاس مساعد (اختياري)
    public static class DriverStatusResponse {
        private String status;
        private String message;
        private DriverModel driverData;

        public DriverStatusResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        public DriverStatusResponse(String status, String message, DriverModel driverData) {
            this.status = status;
            this.message = message;
            this.driverData = driverData;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public DriverModel getDriverData() {
            return driverData;
        }

        public void setDriverData(DriverModel driverData) {
            this.driverData = driverData;
        }
    }
}
