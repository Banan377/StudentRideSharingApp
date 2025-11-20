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
        // 1) تحقق أن الإيميل موجود في جدول الجامعة
        if (!isStudentEmailValid(user.getEmail())) {
            throw new RuntimeException("هذا الإيميل غير مسجّل في قاعدة بيانات الجامعة");
        }

        // 2) تأكد أنه غير مسجل مسبقاً
        if (isUserRegistered(user.getEmail())) {
            throw new RuntimeException("هذا الإيميل مسجل مسبقاً!");
        }

        // 3) حفظ مع تشفير الباسورد
        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 4) حفظ المستخدم
        userRepository.save(user);
        // 5) إنشاء سجل في جدول الركاب للجميع
        PassengerModel passenger = new PassengerModel(user.getEmail());
        passengerRepository.save(passenger);
        // 7) إذا كان سائق، إنشاء سجل في جدول السائقين
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
