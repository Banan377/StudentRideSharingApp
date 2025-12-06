package com.example.rideapp.repositories;

import com.example.rideapp.models.RideModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<RideModel, Long> {
    List<RideModel> findByDestinationContainingIgnoreCaseAndStatus(String destination, String status);

    List<RideModel> findByStatus(String status);

    List<RideModel> findBySeatsAvailableGreaterThan(int seats);

    List<RideModel> findByDriverEmail(String driverEmail);

    void deleteByDriverEmail(String driverEmail);

    List<RideModel> findByDriverEmailAndStatus(String driverEmail, String status);

    List<RideModel> findByDriverEmailAndStatusNot(String email, String status);

}
