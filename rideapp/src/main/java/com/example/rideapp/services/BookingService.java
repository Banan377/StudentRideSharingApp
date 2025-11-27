package com.example.rideapp.services;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.models.PassengerModel;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.BookingRepository;
import com.example.rideapp.repositories.PassengerRepository;
import com.example.rideapp.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final RideService rideService;
    private final PassengerRepository passengerRepository;

    @Autowired
    private UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository,
            RideService rideService,
            PassengerRepository passengerRepository,
            UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.rideService = rideService;
        this.passengerRepository = passengerRepository;
        this.userRepository = userRepository;
    }

    public BookingModel createBookingRequest(Long rideId, String passengerEmail) {

        // 1) جلب الرحلة
        RideModel ride = rideService.getRideById(rideId);
        if (ride == null || ride.getSeatsAvailable() < 1) {
            return null;
        }

        // 2) جلب الراكب من جدول passengers
        PassengerModel passenger = passengerRepository.findById(passengerEmail)
                .orElseThrow(() -> new RuntimeException("Passenger not found: " + passengerEmail));

        // 3) جلب جنس الراكب من جدول users
        UserModel user = userRepository.findByEmail(passengerEmail).orElse(null);
        String passengerGender = (user != null) ? user.getGender() : null;

        // 4) جلب تفضيل السائق
        String pref = ride.getGenderPreference();
        if (pref == null)
            pref = ""; // حماية


        // حالة رحلة للذكور فقط
        if (pref.equalsIgnoreCase("male")) {
            if (passengerGender == null || !passengerGender.equalsIgnoreCase("male")) {
                throw new RuntimeException("هذه الرحلة مخصصة للرجال فقط");
            }
        }

        // حالة رحلة للإناث فقط
        if (pref.equalsIgnoreCase("female")) {
            if (passengerGender == null || !passengerGender.equalsIgnoreCase("female")) {
                throw new RuntimeException("هذه الرحلة مخصصة للنساء فقط");
            }
        }

        // 5) إذا الجنس متوافق → إنشاء الحجز
        BookingModel booking = new BookingModel();
        booking.setRide(ride);
        booking.setPassenger(passenger);
        booking.setPaymentStatus("PENDING");
        booking.setStatus("pending");

        return bookingRepository.save(booking);
    }

    public List<BookingModel> getPendingBookingsByDriver(String driverEmail) {

        List<BookingModel> allBookings = bookingRepository.findAll();

        return allBookings.stream()
                .filter(booking -> booking.getRide() != null &&
                        booking.getRide().getDriverEmail() != null &&
                        booking.getRide().getDriverEmail().equals(driverEmail) &&
                        "pending".equals(booking.getStatus()))
                .peek(booking -> {

                    PassengerModel passengerFull = passengerRepository.findByEmail(booking.getPassenger().getEmail())
                            .orElse(null);

                    UserModel user = userRepository.findByEmail(booking.getPassenger().getEmail())
                            .orElse(null);

                    if (user != null) {
                        booking.getPassenger().setName(user.getName());
                    }

                })
                .collect(Collectors.toList());
    }

    public boolean respondToBooking(Long bookingId, String status) {
        return bookingRepository.findById(bookingId).map(booking -> {
            booking.setStatus(status);

            if ("accepted".equalsIgnoreCase(status)) {
                rideService.decreaseSeats(booking.getRide(), 1);
            }

            bookingRepository.save(booking);
            return true;
        }).orElse(false);
    }
}
