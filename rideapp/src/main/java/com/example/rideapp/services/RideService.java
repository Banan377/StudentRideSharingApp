package com.example.rideapp.services;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.BookingRepository;
import com.example.rideapp.repositories.RideRepository;
import com.example.rideapp.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class RideService {

    @Autowired
    private BookingRepository bookingRepository;

    private final RideRepository rideRepository;

    public RideService(RideRepository rideRepository) {
        this.rideRepository = rideRepository;
    }

    public RideModel createRide(RideModel ride) {
        // حفظ ايميل السائق داخل الرحلة
        ride.setDriverEmail(ride.getDriverEmail());
        ride.setTotalSeats(ride.getSeatsAvailable());

        ride.setStatus("ACTIVE");
        return rideRepository.save(ride);

    }

    public boolean cancelRide(Long rideId) {
        return rideRepository.findById(rideId).map(ride -> {
            ride.setStatus("CANCELLED");
            rideRepository.save(ride);
            return true;
        }).orElse(false);
    }

    public List<RideModel> getAllAvailableRides() {
        return rideRepository.findBySeatsAvailableGreaterThan(0);
    }

    public List<RideModel> searchRides(String keyword) {
        return rideRepository.findByDestinationContainingIgnoreCaseAndStatus(keyword, "ACTIVE");
    }

    public RideModel getRideById(Long id) {
        return rideRepository.findById(id).orElse(null);
    }

    public void decreaseSeats(RideModel ride, int seats) {
        ride.setSeatsAvailable(ride.getSeatsAvailable() - seats);
        rideRepository.save(ride);
    }

    // إضافة الدالة الجديدة:
    public List<RideModel> getAllRides() {
        return rideRepository.findAll();
    }

    public List<RideModel> getAvailableRidesForPassenger(String passengerEmail) {

        // كل الرحلات
        List<RideModel> allRides = rideRepository.findAll();

        // كل الحجوزات الخاصة بهذا الراكب
        List<BookingModel> booked = bookingRepository.findByPassenger_Email(passengerEmail);

        // IDs تبع الرحلات اللي حجزها مسبقاً
        Set<Long> bookedRideIds = booked.stream()
                .map(b -> b.getRide().getRideId())
                .collect(Collectors.toSet());

        // فلترة الرحلات
        List<RideModel> rides = allRides.stream()
                .filter(ride -> ride.getSeatsAvailable() > 0)
                .filter(ride -> !bookedRideIds.contains(ride.getRideId()))
                .collect(Collectors.toList());

        // إضافة اسم السائق
        rides.forEach(ride -> {
            UserModel driver = userRepository.findByEmail(ride.getDriverEmail()).orElse(null);
            if (driver != null) {
                ride.setDriverName(driver.getName());
                ride.setDriverRating(driver.getRateAverage());
            }
        });

        return rides;
    }

    public void updateRideLocation(Long rideId, String location) {
        rideRepository.findById(rideId).ifPresent(ride -> {
            ride.setCurrentLocation(location);
            rideRepository.save(ride);
        });
    }

    @Autowired
    private UserRepository userRepository;

    public List<RideModel> getPastRidesForPassenger(String passengerEmail) {

        // 1) جلب كل الحجوزات المقبولة للراكب
        List<BookingModel> bookings = bookingRepository.findByPassenger_Email(passengerEmail)
                .stream()
                .filter(b -> "accepted".equals(b.getStatus())) // الراكب كان راكب فعلاً
                .filter(b -> b.getRide() != null) // حماية من null
                .filter(b -> "COMPLETED".equals(b.getRide().getStatus()))
                .collect(Collectors.toList());

        // 2) تحويل الحجوزات إلى رحلات
        List<RideModel> rides = bookings.stream()
                .map(BookingModel::getRide)
                .collect(Collectors.toList());

        // 3) إضافة معلومات السائق
        rides.forEach(ride -> {
            UserModel driver = userRepository.findByEmail(ride.getDriverEmail()).orElse(null);
            if (driver != null) {
                ride.setDriverName(driver.getName());
                ride.setDriverRating(driver.getRateAverage());
            }
        });

        return rides;
    }

    public List<RideModel> getRidesForDriver(String driverEmail) {
        return rideRepository.findByDriverEmail(driverEmail);
    }

    public boolean completeRide(Long rideId) {
        return rideRepository.findById(rideId).map(ride -> {
            ride.setStatus("COMPLETED"); 
            rideRepository.save(ride);
            return true;
        }).orElse(false);
    }

    public List<RideModel> getPastRidesForDriver(String driverEmail) {

        // 1) جلب كل رحلات السائق
        List<RideModel> rides = rideRepository.findByDriverEmail(driverEmail)
                .stream()
                .filter(r -> "COMPLETED".equalsIgnoreCase(r.getStatus())) 
                .collect(Collectors.toList());

      
        rides.forEach(ride -> {
            UserModel driver = userRepository.findByEmail(ride.getDriverEmail()).orElse(null);
            if (driver != null) {
                ride.setDriverName(driver.getName());
                ride.setDriverRating(driver.getRateAverage());
            }
        });

        return rides;
    }

}
