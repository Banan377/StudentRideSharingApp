package com.example.rideapp.services;


import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.rideapp.models.MapModel;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.repositories.MapRepository;
import com.example.rideapp.repositories.RideRepository;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class LocationService {
  @Autowired private MapRepository mapRepo;
  @Autowired private RideRepository rideRepo;
  @Autowired private SimpMessagingTemplate messaging;

  public MapModel addLocation(Long rideId, MapModel m) {
    RideModel ride = rideRepo.findById(rideId).orElse(null);
    if (ride == null) throw new RuntimeException("الرحلة غير موجودة");
    MapModel map = new MapModel();
    map.setRide(ride);
    map.setLatitude(m.getLatitude());
    map.setLongitude(m.getLongitude());
    map.setTimestamp(LocalDateTime.now());
    MapModel saved = mapRepo.save(map);
    // broadcast to subscribers
    messaging.convertAndSend("/topic/ride/" + rideId + "/location", saved);
    // update ride current location
    ride.setCurrentLocation(m.getLatitude() + "," + m.getLongitude());
    rideRepo.save(ride);
    return saved;
  }
}
