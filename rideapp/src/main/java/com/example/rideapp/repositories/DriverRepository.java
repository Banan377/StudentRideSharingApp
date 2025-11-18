package com.example.rideapp.repositories;

import com.example.rideapp.models.DriverModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DriverRepository extends JpaRepository<DriverModel, String> {
    Optional<DriverModel> findByEmail(String email);
    boolean existsByEmail(String email);
}