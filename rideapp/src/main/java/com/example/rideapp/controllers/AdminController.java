package com.example.rideapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.rideapp.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    // جلب كل الطلبات المعلقة
    @GetMapping("/pending-drivers")
    public ResponseEntity<?> getPendingDrivers() {
        return ResponseEntity.ok(adminService.getPendingDrivers());
    }

    // قبول
    @PutMapping("/approve")
    public ResponseEntity<?> approveDriver(@RequestParam String email) {
        boolean success = adminService.updateDriverStatus(email, "approved");
        if (!success)
            return ResponseEntity.badRequest().body("Driver not found");
        return ResponseEntity.ok("Approved");
    }

    // رفض
    @PutMapping("/reject")
    public ResponseEntity<?> rejectDriver(@RequestParam String email) {
        boolean success = adminService.updateDriverStatus(email, "rejected");
        if (!success)
            return ResponseEntity.badRequest().body("Driver not found");
        return ResponseEntity.ok("Rejected");
    }
  
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @PutMapping("/users/status")
    public ResponseEntity<?> updateUserStatus(
            @RequestParam String email,
            @RequestParam String status) { // "active" / "suspended" / "inactive"
        boolean success = adminService.updateUserStatus(email, status);
        if (!success) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok("Status updated to: " + status);
    }

    @DeleteMapping("/users")
    public ResponseEntity<?> deleteUser(@RequestParam String email) {
        boolean success = adminService.deleteUser(email);
        if (!success) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok("User deleted");
    }
}
