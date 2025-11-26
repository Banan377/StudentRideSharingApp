package com.example.rideapp.services;

import com.example.rideapp.models.BookingModel;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.repositories.BookingRepository;
import com.example.rideapp.repositories.RideRepository;

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

        // ارجاع الرحلات:
        // 1) اللي فيها مقاعد
        // 2) الراكب ما حجزها قبل
        return allRides.stream()
                .filter(ride -> ride.getSeatsAvailable() > 0)
                .filter(ride -> !bookedRideIds.contains(ride.getRideId()))
                .collect(Collectors.toList());
    }

    public void updateRideLocation(Long rideId, String location) {
        rideRepository.findById(rideId).ifPresent(ride -> {
            ride.setCurrentLocation(location);
            rideRepository.save(ride);
        });
    }

}
