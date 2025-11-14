package com.example.rideapp.services;

import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    // إنشاء حساب جديد
    public UserModel createAccount(UserModel user) {
        return userRepository.save(user);
    }

    // التحقق من البريد وكلمة المرور
    public boolean login(String email, String password) {
        UserModel user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

}

