package com.example.rideapp.controllers;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.services.BookingService;
import com.example.rideapp.services.RideService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class RideBookingController {

    private final BookingService bookingService;
    private final RideService rideService;

    public RideBookingController(BookingService bookingService, RideService rideService) {
        this.bookingService = bookingService;
        this.rideService = rideService;
    }

    // ====== طلب حجز من الراكب (زر حجز الآن في bookingPage) ======
    @PostMapping("/request")
    public ResponseEntity<?> createBooking(
            @RequestParam String passengerEmail,
            @RequestParam Long rideId) {
        try {
            BookingModel booking = bookingService.createBookingRequest(rideId, passengerEmail);
            return ResponseEntity.ok(booking);

        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(400)
                    .body(ex.getMessage()); // هنا ترجع رسالة واضحة
        }
    }

    // ====== جلب الطلبات المعلّقة للسائق (تستخدم في driverUI.html) ======
    @GetMapping("/driver-requests")
    public ResponseEntity<List<BookingModel>> getDriverRequests(
            @RequestParam String driverEmail) {

        List<BookingModel> requests = bookingService.getPendingBookingsByDriver(driverEmail);
        return ResponseEntity.ok(requests);
    }

    // ====== رد السائق على الطلب (قبول / رفض) ======
    @PostMapping("/respond")
    public ResponseEntity<Void> respondToBooking(
            @RequestParam Long bookingId,
            @RequestParam String status) {

        boolean ok = bookingService.respondToBooking(bookingId, status);
        if (ok) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/accepted-passengers")
    public ResponseEntity<List<BookingModel>> getAcceptedPassengers(
            @RequestParam Long rideId) {
        return ResponseEntity.ok(bookingService.getAcceptedPassengersForRide(rideId));
    }

    @GetMapping("/ride-cancelled")
    public ResponseEntity<Void> notifyCancelled(@RequestParam Long rideId) {
        bookingService.notifyPassengersRideCancelled(rideId);
        return ResponseEntity.ok().build();
    }

}
