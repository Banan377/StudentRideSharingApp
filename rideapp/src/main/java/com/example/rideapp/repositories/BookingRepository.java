package com.example.rideapp.repositories;

import com.example.rideapp.models.BookingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookingRepository extends JpaRepository<BookingModel, Long> {
    List<BookingModel> findByPassenger_Email(String email);

    @Query("SELECT b FROM BookingModel b WHERE b.ride.driverEmail = :driverEmail AND b.status = :status")
    List<BookingModel> findByDriverEmailAndStatus(@Param("driverEmail") String driverEmail,
            @Param("status") String status);

    void deleteByPassengerEmail(String passengerEmail);

    List<BookingModel> findByRide_RideIdAndStatus(Long rideId, String status);

}
