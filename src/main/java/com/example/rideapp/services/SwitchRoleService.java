package com.example.rideapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.repositories.DriverRepository;

@Service
public class SwitchRoleService {

    @Autowired
    private DriverRepository driverRepo;

    public boolean verifyDriver(String email) {
        DriverModel driver = driverRepo.findByEmail(email);
        return driver != null && "approved".equalsIgnoreCase(driver.getStatus());
    }

    public String checkRole(String email) {
        DriverModel driver = driverRepo.findByEmail(email);

        if (driver == null) return "no_driver_data";

        if (!"approved".equalsIgnoreCase(driver.getStatus())) return "not_verified";

        return "verified_driver";
    }

    public String becomeDriver(String email) {
        DriverModel driver = driverRepo.findByEmail(email);

        if (driver == null) return "missing_documents";

        if (!"approved".equalsIgnoreCase(driver.getStatus())) return "pending";

        return "switch_to_driver";
    }

    public String becomePassenger(String email) {
        return "switch_to_passenger";
    }
}
