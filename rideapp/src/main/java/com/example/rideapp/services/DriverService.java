package com.example.rideapp.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.repositories.DriverRepository;

@Service
public class DriverService {
    
    @Autowired
    private DriverRepository driverRepository;
    
    public void createDriverRecord(String email, Map<String, Object> driverData) {
        DriverModel driver = new DriverModel(
            email,
            (String) driverData.get("licenseNumber"),
            "", // سيتم تعبئتها لاحقاً
            "", // سيتم تعبئتها لاحقاً
            (String) driverData.get("carModel"),
            (String) driverData.get("carColor"),
            (String) driverData.get("carPlate"),
            (int) driverData.get("seatsAvailable")
        );
        
        driverRepository.save(driver);
    }
}