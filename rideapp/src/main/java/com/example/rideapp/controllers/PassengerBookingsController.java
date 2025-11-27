package com.example.rideapp.controllers;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.BookingRepository;
import com.example.rideapp.repositories.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passenger")
public class PassengerBookingsController {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public PassengerBookingsController(BookingRepository bookingRepository,
                                       UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingModel>> getMyBookings(
            @RequestParam String passengerEmail) {

        List<BookingModel> bookings = bookingRepository.findByPassenger_Email(passengerEmail);

        bookings.forEach(booking -> {
            if (booking.getRide() != null) {
                String driverEmail = booking.getRide().getDriverEmail();
                if (driverEmail != null) {
                    UserModel driver = userRepository.findByEmail(driverEmail).orElse(null);
                    if (driver != null) {
                        booking.getRide().setDriverName(driver.getName());
                        booking.getRide().setDriverRating(driver.getRateAverage());
                    }
                }
            }
        });

        return ResponseEntity.ok(bookings);
    }
}
