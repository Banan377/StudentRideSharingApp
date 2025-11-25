package com.example.rideapp.services;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.models.PassengerModel;
import com.example.rideapp.models.UserModel;
import com.example.rideapp.repositories.DriverRepository;
import com.example.rideapp.repositories.PassengerRepository;
import com.example.rideapp.repositories.StudentRepository;
import com.example.rideapp.repositories.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private DriverService driverService;

    public boolean isUserRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isStudentEmailValid(String email) {
        return studentRepository.existsByEmail(email);
    }

    public void createAccount(UserModel user, Map<String, Object> driverData) {
        if (!isStudentEmailValid(user.getEmail())) {
            throw new RuntimeException("هذا الإيميل غير مسجّل في قاعدة بيانات الجامعة");
        }

        if (isUserRegistered(user.getEmail())) {
            throw new RuntimeException("هذا الإيميل مسجل مسبقاً!");
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        if (driverData != null && !driverData.isEmpty()) {
            user.setStatus("pending"); 
        } else {
            user.setStatus("active");
        }

        userRepository.save(user);

        if (!passengerRepository.existsById(user.getEmail())) {
            PassengerModel passenger = new PassengerModel(user.getEmail());
            passengerRepository.save(passenger);
        }

        if (driverData != null && !driverData.isEmpty()) {
            DriverModel driver = new DriverModel(
                    user.getEmail(),
                    (String) driverData.get("licenseNumber"),
                    "",
                    "",
                    (String) driverData.get("carModel"),
                    (String) driverData.get("carColor"),
                    (String) driverData.get("carPlate"),
                    (int) driverData.get("seatsAvailable"));
            driver.setStatus("pending");
            driverRepository.save(driver);
        }
    }

    public void updatePassword(String email, String newPassword) {
        Optional<UserModel> userOpt = userRepository.findById(email);

        if (userOpt.isEmpty()) {
            throw new RuntimeException("المستخدم غير موجود");
        }

        UserModel user = userOpt.get();
        String encoded = new BCryptPasswordEncoder().encode(newPassword);
        user.setPassword(encoded);
        userRepository.save(user);
    }
}
