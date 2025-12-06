package com.example.rideapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.BookingRepository;
import com.example.rideapp.repositories.DriverRepository;
import com.example.rideapp.repositories.RideRepository;
import com.example.rideapp.repositories.UserRepository;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private DriverRepository driverRepository;

    public List<DriverModel> getPendingDrivers() {
        return driverRepository.findByStatus("pending");
    }

    // تحديث حالة السائق
    public boolean updateDriverStatus(String email, String status) {

        DriverModel driver = driverRepository.findByEmail(email);

        if (driver == null) {
            return false;
        }

        driver.setStatus(status);
        driverRepository.save(driver);

        return true;
    }

    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    // تحديث حالة المستخدم
    public boolean updateUserStatus(String email, String status) {
        return userRepository.findById(email).map(user -> {
            user.setStatus(status);
            userRepository.save(user);
            return true;
        }).orElse(false);
    }

    // حذف المستخدم (انتِ تقررين متى تستخدمينه)
    public boolean deleteUser(String email) {
        if (!userRepository.existsById(email)) {
            return false;
        }

        // احذفي كل الأشياء المرتبطة (حسب تصميمكم)
        bookingRepository.deleteByPassengerEmail(email);
        rideRepository.deleteByDriverEmail(email);
        driverRepository.deleteByEmail(email);

        userRepository.deleteById(email);
        return true;
    }
}