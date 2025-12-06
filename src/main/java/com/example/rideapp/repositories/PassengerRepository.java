package com.example.rideapp.repositories;

import com.example.rideapp.models.PassengerModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PassengerRepository extends JpaRepository<PassengerModel, String> {
    Optional<PassengerModel> findByEmail(String email);
    boolean existsByEmail(String email);
}