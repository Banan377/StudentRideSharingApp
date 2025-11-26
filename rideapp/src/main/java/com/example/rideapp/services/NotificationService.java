package com.example.rideapp.services;

import org.springframework.stereotype.Service;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.models.SafetyModel;

@Service
public class NotificationService {
  public void sendEmergencyNotification(UserModel user, SafetyModel alert) {
    String contact = user == null ? null : user.getEmergencyContacts();
    // integrate Twilio For now log.
    System.out.println("Notify emergency contact: " + contact + " message: " + alert.getMessage());
  }

  public void sendLocationUpdateToEmergencyContact(UserModel user, String location) {
    String contact = user == null ? null : user.getEmergencyContacts();
    System.out.println("Location update to " + contact + " -> " + location);
  }
}
