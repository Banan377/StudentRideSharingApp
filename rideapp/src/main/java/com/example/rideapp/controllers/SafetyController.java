package com.example.rideapp.controllers;

import com.example.rideapp.models.SafetyModel;
import com.example.rideapp.services.SafetyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/safety")
public class SafetyController {

    @Autowired
    private SafetyService safetyService;

    @PostMapping("/trigger")
    public ResponseEntity<?> triggerAlert(
            @RequestParam(required = false) Long rideId, //  rideId OPTIONAL
            @RequestParam String userId,
            @RequestParam(required = false) String message,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng) {

        SafetyModel alert = safetyService.triggerAlert(rideId, userId, message, lat, lng);

        return ResponseEntity.ok(alert);
    }

}
