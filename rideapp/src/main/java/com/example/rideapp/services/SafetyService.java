package com.example.rideapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import com.example.rideapp.models.RideModel;
import com.example.rideapp.models.SafetyModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.SafetyRepository;
import java.time.LocalDateTime;

@Service
public class SafetyService {
  @Autowired private SafetyRepository safetyRepo;
  @Autowired private SimpMessagingTemplate messaging;
  @Autowired private UserService userService;
  @Autowired private RideService rideService;
  @Autowired private NotificationService notificationService;

  public SafetyModel triggerAlert(Long rideId, String userId, String message, Double lat, Double lng) {
    RideModel ride = rideService.getRideById(rideId);
    UserModel user = userService.getUser(userId);
    SafetyModel s = new SafetyModel();
    s.setRide(ride);
    s.setTriggeredBy(user);
    s.setMessage(message);
    s.setLatitude(lat);
    s.setLongitude(lng);
    s.setStatus("TRIGGERED");
    s.setCreatedAt(LocalDateTime.now());
    SafetyModel saved = safetyRepo.save(s);
    // broadcast
    messaging.convertAndSend("/topic/ride/" + rideId + "/safety", saved);
   // notify emergency contact (can be Twilio)
    notificationService.sendEmergencyNotification(user, saved);
    return saved;
  }
}
