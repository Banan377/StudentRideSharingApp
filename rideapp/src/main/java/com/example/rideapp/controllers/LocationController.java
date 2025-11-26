package com.example.rideapp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rideapp.models.MapModel;
import com.example.rideapp.services.LocationService;

@RestController
@RequestMapping("/api/locations")
public class LocationController {
    @Autowired private LocationService locationService;

    @PostMapping("/ride/{rideId}")
    public ResponseEntity<?> post(@PathVariable Long rideId, @RequestBody MapModel map) {
        return ResponseEntity.ok(locationService.addLocation(rideId, map));
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<?> get(@PathVariable Long rideId) {
        return ResponseEntity.ok(locationService.getLocations(rideId));
    }
}
