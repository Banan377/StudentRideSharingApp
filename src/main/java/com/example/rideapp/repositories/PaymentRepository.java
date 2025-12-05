package com.example.rideapp.repositories;

import com.example.rideapp.models.PaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentModel, Long> {
    List<PaymentModel> findByRideId(Long rideId);
    List<PaymentModel> findByPassengerId(Long passengerId);
    List<PaymentModel> findByStatus(String status);
}