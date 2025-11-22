package com.example.rideapp.services;

import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.UserRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    // دالة جلب بيانات المستخدم
    public UserModel getUserProfile(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean updateProfile(String email, String college, String emergencyContacts, String password) {
        try {
            UserModel user = userRepository.findByEmail(email);
            if (user == null)
                return false;

            if (college != null)
                user.setCollege(college);
            if (emergencyContacts != null)
                user.setEmergencyContacts(emergencyContacts);
            if (password != null && !password.trim().isEmpty())
                user.setPassword(password);

            userRepository.save(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}