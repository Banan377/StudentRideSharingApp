package com.example.rideapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.rideapp.models.SafetyModel;
import java.util.List;

@Repository
public interface SafetyRepository extends JpaRepository<SafetyModel, Long> {
     List<SafetyModel> findByRide_RideId(Long rideId);
}
