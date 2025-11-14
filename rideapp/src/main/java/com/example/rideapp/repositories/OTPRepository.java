package com.example.rideapp.repositories;

import com.example.rideapp.models.OTPModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OTPRepository extends JpaRepository<OTPModel, Long> {
    Optional<OTPModel> findByEmail(String email);
}
