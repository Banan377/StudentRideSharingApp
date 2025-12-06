package com.example.rideapp.controllers;

import com.example.rideapp.models.RideModel;
import com.example.rideapp.services.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rides")
public class RideCreationController {

    private final RideService rideService;

    public RideCreationController(RideService rideService) {
        this.rideService = rideService;
    }

    // إنشاء رحلة جديدة
    @PostMapping("/createRide")
    public ResponseEntity<RideModel> createRide(
            @RequestBody RideModel ride,
            @RequestParam String driverEmail) {

        ride.setDriverEmail(driverEmail);
        RideModel savedRide = rideService.createRide(ride);

        return ResponseEntity.ok(savedRide);

    }

    // إلغاء الرحلة
    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancelRide(@PathVariable Long id) {
        boolean cancelled = rideService.cancelRide(id);
        if (cancelled) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // عرض كل الرحلات المتاحة (للبوكينق بيج)
    @GetMapping("/available")
    public List<RideModel> getAvailableRidesForPassenger(@RequestParam String passengerEmail) {
        return rideService.getAvailableRidesForPassenger(passengerEmail);
    }

    // البحث في الرحلات
    @GetMapping("/search")
    public ResponseEntity<List<RideModel>> searchRides(@RequestParam String destination) {
        return ResponseEntity.ok(rideService.searchRides(destination));
    }

    // الرحلات السابقة
    @GetMapping("/past")
    public ResponseEntity<List<RideModel>> getPastRides(
            @RequestParam String passengerEmail) {

        List<RideModel> rides = rideService.getPastRidesForPassenger(passengerEmail);
        return ResponseEntity.ok(rides);
    }

    // جلب رحلات السائق
    @GetMapping("/driver-rides")
    public ResponseEntity<List<RideModel>> getDriverRides(@RequestParam String email) {
        List<RideModel> rides = rideService.getRidesForDriver(email);
        return ResponseEntity.ok(rides);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Void> completeRide(@PathVariable Long id) {
        boolean completed = rideService.completeRide(id);
        if (completed) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // الرحلات السابقة للسائق
    @GetMapping("/driver-past")
    public ResponseEntity<List<RideModel>> getDriverPastRides(@RequestParam String email) {

        List<RideModel> rides = rideService.getPastRidesForDriver(email);
        return ResponseEntity.ok(rides);
    }

}
