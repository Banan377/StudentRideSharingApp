package com.example.rideapp.controllers;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.repositories.BookingRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerBookingsController {

    private final BookingRepository bookingRepository;

    public PassengerBookingsController(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingModel>> getMyBookings(
            @RequestParam String passengerEmail) {

        List<BookingModel> bookings = bookingRepository.findByPassenger_Email(passengerEmail);
        return ResponseEntity.ok(bookings);
    }
}
