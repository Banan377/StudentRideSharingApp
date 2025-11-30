package com.example.rideapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.rideapp.models.MapModel;
import java.util.List;

@Repository
public interface MapRepository extends JpaRepository<MapModel, Long> {
   List<MapModel> findByRide_RideIdOrderByTimestampAsc(Long rideId); 
}
