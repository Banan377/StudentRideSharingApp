package com.example.rideapp.services;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepo;

    public DriverModel getDriver(String email) {
        return driverRepo.findByEmail(email);
    }

    public boolean updateDriver(DriverModel updatedDriver) {
        DriverModel driver = driverRepo.findByEmail(updatedDriver.getEmail());
        if (driver == null) return false;

        // تحديث جميع الحقول المطلوبة
        if (updatedDriver.getCarModel() != null) {
            driver.setCarModel(updatedDriver.getCarModel());
        }
        if (updatedDriver.getCarColor() != null) {
            driver.setCarColor(updatedDriver.getCarColor());
        }
        if (updatedDriver.getSeatsAvailable() > 0) {
            driver.setSeatsAvailable(updatedDriver.getSeatsAvailable());
        }
        if (updatedDriver.getLicenseNumber() != null) {
            driver.setLicenseNumber(updatedDriver.getLicenseNumber());
        }

        driverRepo.save(driver);
        return true;
    }
}
