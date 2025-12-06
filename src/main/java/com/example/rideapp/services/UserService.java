package com.example.rideapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.UserRepository;
import java.util.Optional;

@Service
public class UserService {
    @Autowired private UserRepository userRepository;
    
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserModel getUser(String userEmail) {
       return userRepository.findByEmail(userEmail).orElse(null);
    }

    public void updateLocation(String userEmail, String location) {
    UserModel u = userRepository.findById(userEmail).orElse(null);
    if (u != null) { u.setCurrentLocation(location); userRepository.save(u); }
    }
}
