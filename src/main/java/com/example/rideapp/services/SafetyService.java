package com.example.rideapp.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.rideapp.models.RideModel;
import com.example.rideapp.models.SafetyModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.SafetyRepository;

@Service
public class SafetyService {

  @Autowired
  private SafetyRepository safetyRepo;
  @Autowired
  private SimpMessagingTemplate messaging;
  @Autowired
  private UserService userService;
  @Autowired
  private RideService rideService;
  @Autowired
  private NotificationService notificationService;

  public SafetyModel triggerAlert(Long rideId, String userId, String message, Double lat, Double lng) {

    UserModel user = userService.getUser(userId);

    RideModel ride = null;
    if (rideId != null) {
      ride = rideService.getRideById(rideId);
    }
    SafetyModel s = new SafetyModel();
    s.setRide(ride);
    s.setTriggeredBy(user);
    s.setMessage(message);
    s.setLatitude(lat);
    s.setLongitude(lng);
    s.setStatus("TRIGGERED");
    s.setCreatedAt(LocalDateTime.now());

    SafetyModel saved = safetyRepo.save(s);

    // إرسال SMS لأول رقم طوارئ
    String contacts = user.getEmergencyContacts();
    String firstContact = null;

    if (contacts != null && !contacts.isEmpty()) {
      firstContact = contacts.split(",")[0].trim();
    }

    if (firstContact != null && !firstContact.isEmpty()) {
      if (firstContact.startsWith("0")) {
        firstContact = "+966" + firstContact.substring(1);
      }

      notificationService.sendEmergencySMSTo(firstContact, user.getName(), rideId);
    }

    return saved;
  }

}
