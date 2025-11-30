package com.example.rideapp.services;

import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationService {

    // رقم الطوارئ
    private final String EMERGENCY_PHONE = "+966557910117";

    // رقم Twilio الخاص بك
    private final String TWILIO_NUMBER = "+12679152009";

    public void sendEmergencySMS(String passengerName, Long rideId) {

        String body = " Emergency Alert!\n"
                + "Passenger: " + passengerName + "\n"
                + "Ride ID: " + rideId + "\n"
                + "Needs immediate assistance.";

        Message.creator(
                new PhoneNumber(EMERGENCY_PHONE), // to
                new PhoneNumber(TWILIO_NUMBER), // from
                body).create();

        System.out.println("SMS Sent Successfully to " + EMERGENCY_PHONE);
    }

    public void sendEmergencySMSTo(String phone, String passengerName, Long rideId) {
        try {
            String body = " Emergency Alert!\n"
                    + "Passenger: " + passengerName + "\n"
                    + "Ride ID: " + rideId + "\n"
                    + "Needs immediate assistance.";

            Message.creator(
                    new PhoneNumber(phone), 
                    new PhoneNumber("+12679152009"),
                    body).create();

            System.out.println("SMS Sent Successfully to " + phone);

        } catch (Exception e) {
            System.out.println(" ERROR SENDING SMS:");
            e.printStackTrace();
        }
    }

}
