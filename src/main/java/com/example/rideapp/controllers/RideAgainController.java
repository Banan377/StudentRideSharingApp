package com.example.rideapp.controllers;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.models.PassengerModel;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.BookingRepository;
import com.example.rideapp.repositories.PassengerRepository;
import com.example.rideapp.repositories.RideRepository;
import com.example.rideapp.repositories.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ride-again")
public class RideAgainController {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final RideRepository rideRepository;
        private final UserRepository userRepository;


    public RideAgainController(BookingRepository bookingRepository,
            PassengerRepository passengerRepository,
            RideRepository rideRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.rideRepository = rideRepository;
        this.userRepository=userRepository;
    }

    // ========== طلب إعادة ركوب من الراكب ==========
    @PostMapping("/request")
    public ResponseEntity<?> requestRideAgain(
            @RequestParam String passengerEmail,
            @RequestParam Long rideId) {

        RideModel oldRide = rideRepository.findById(rideId).orElse(null);
        if (oldRide == null) {
            return ResponseEntity.badRequest().body("Ride not found");
        }

        PassengerModel passenger = passengerRepository.findById(passengerEmail).orElse(null);
        if (passenger == null) {
            return ResponseEntity.badRequest().body("Passenger not found");
        }

        BookingModel request = new BookingModel();
        request.setRide(oldRide);
        request.setPassenger(passenger);
        request.setStatus("repeat_request");
        request.setPaymentStatus("PENDING");

        bookingRepository.save(request);

        return ResponseEntity.ok("Ride Again request sent!");
    }

    // ========== جلب طلبات إعادة الركوب للسائق ==========
    @GetMapping("/driver-requests")
    public ResponseEntity<?> getDriverRepeatRequests(@RequestParam Long rideId) {

        List<BookingModel> requests = bookingRepository.findAll().stream()
                .filter(b -> b.getRide() != null &&
                        b.getRide().getRideId().equals(rideId) &&
                        "repeat_request".equals(b.getStatus()))
                .collect(Collectors.toList());

        // إضافة الاسم الحقيقي للراكب
        requests.forEach(req -> {
            UserModel user = userRepository.findByEmail(req.getPassenger().getEmail()).orElse(null);
            if (user != null) {
                req.getPassenger().setName(user.getName());
            }

        });

        return ResponseEntity.ok(requests);
    }

    // ========== رد السائق على الطلب ==========
    @PostMapping("/respond")
    public ResponseEntity<?> respondRepeatRequest(
            @RequestParam Long bookingId,
            @RequestParam String status) {

        return bookingRepository.findById(bookingId).map(req -> {

            req.setStatus(status);
            bookingRepository.save(req);

            return ResponseEntity.ok("Request " + status);

        }).orElse(ResponseEntity.badRequest().body("Booking not found"));
    }

}
