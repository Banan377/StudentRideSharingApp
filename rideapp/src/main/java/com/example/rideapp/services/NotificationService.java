package com.example.rideapp.services;

import com.example.rideapp.models.SafetyModel;
import com.example.rideapp.models.UserModel;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    // ضعي معلومات Twilio هنا
    private static final String ACCOUNT_SID = "YOUR_TWILIO_SID";
    private static final String AUTH_TOKEN = "YOUR_TWILIO_AUTH_TOKEN";
    private static final String FROM_NUMBER = "+1234567890"; // رقم Twilio اللي اشتريتيه

    public void sendEmergencyNotification(UserModel user, SafetyModel alert) {

        if (user.getEmergencyContacts() == null || user.getEmergencyContacts().isEmpty()) {
            System.out.println("لا يوجد رقم للطوارئ لهذا الراكب.");
            return;
        }

        String emergencyNumber = user.getEmergencyContacts();

        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        String msg = "🚨 Emergency Alert!\n"
                + "Passenger: " + user.getName() + "\n"
                + "Needs help during a ride.\n"
                + "Location: https://maps.google.com/?q="
                + alert.getLatitude() + "," + alert.getLongitude();

        Message.creator(
                new com.twilio.type.PhoneNumber(emergencyNumber),
                new com.twilio.type.PhoneNumber(FROM_NUMBER),
                msg
        ).create();

        System.out.println("Emergency SMS sent to: " + emergencyNumber);
    }
}
