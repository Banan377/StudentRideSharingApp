package com.example.rideapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.rideapp.models.SafetyModel;
import java.util.List;

public interface SafetyRepository extends JpaRepository<SafetyModel, Long> {
     List<SafetyModel> findByRide_RideId(Long rideId);
}
