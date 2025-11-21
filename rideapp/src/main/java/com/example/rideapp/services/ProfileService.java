package com.example.rideapp.services;

import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    // عرض بيانات المستخدم
    public UserModel getUserProfile(String email) {
        return userRepository.findById(email).orElse(null);
    }

    // تعديل بيانات المستخدم
    public boolean updateProfile(String email, String college, String emergencyContact, String password) {

        UserModel user = userRepository.findById(email).orElse(null);

        if (user == null) return false;

        if (college != null && !college.isEmpty()) {
            user.setCollege(college);
        }

        if (emergencyContact != null && !emergencyContact.isEmpty()) {
            user.setEmergencyContacts(emergencyContact);
        }

        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }

        userRepository.save(user);
        return true;
    }
}
