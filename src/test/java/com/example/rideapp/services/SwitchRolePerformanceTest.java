package com.example.rideapp.services;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.repositories.DriverRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SwitchRolePerformanceTest {

    @Mock
    DriverRepository driverRepo;

    @InjectMocks
    SwitchRoleService switchRoleService;

    @Test
    void testSwitchRolePerformance() {
        DriverModel driver = new DriverModel();
        driver.setStatus("approved");

        when(driverRepo.findByEmail("test@sm.imamu.edu.sa")).thenReturn(driver);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000; i++) {
            switchRoleService.becomeDriver("test@sm.imamu.edu.sa");
        }

        long end = System.currentTimeMillis();

        System.out.println("Non-Functional Performance Test: 1000 role checks completed in " + (end - start) + " ms");
    }
}