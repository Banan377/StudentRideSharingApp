package com.example.rideapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.example.rideapp.models.MapModel;
import com.example.rideapp.models.RideModel;

public interface MapRepository extends JpaRepository<MapModel, Long> {
   List<MapModel> findByRide_RideIdOrderByTimestampAsc(Long rideId); 
}
