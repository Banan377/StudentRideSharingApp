package com.example.rideapp.services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Map<String, Object>> getPendingDrivers() {

        List<DriverModel> drivers = driverRepository.findByStatus("pending");

        return drivers.stream().map(driver -> {
            Map<String, Object> map = new HashMap<>();

            // نجيب بيانات المستخدم
            UserModel user = userRepository.findByEmail(driver.getEmail()).orElse(null);

            map.put("email", driver.getEmail());
            map.put("name", user != null ? user.getName() : "غير معروف");
            map.put("carModel", driver.getCarModel());
            map.put("carColor", driver.getCarColor());

            return map;
        }).collect(Collectors.toList());
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

        bookingRepository.deleteByPassengerEmail(email);
        rideRepository.deleteByDriverEmail(email);
        driverRepository.deleteByEmail(email);

        userRepository.deleteById(email);
        return true;
    }

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long pendingDrivers = driverRepository.countByStatus("pending");
        long approvedDrivers = driverRepository.countByStatus("approved");
        long totalUsers = userRepository.count();

        long todayRides = rideRepository.countByDate(LocalDate.now());

        stats.put("pendingDrivers", pendingDrivers);
        stats.put("approvedDrivers", approvedDrivers);
        stats.put("totalUsers", totalUsers);
        stats.put("todayRides", todayRides);

        return stats;
    }

}