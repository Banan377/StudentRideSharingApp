package com.example.rideapp.services;

import com.example.rideapp.models.DriverModel;
import com.example.rideapp.repositories.DriverRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SwitchRoleServiceTest {

    @Mock
    DriverRepository driverRepo;

    @InjectMocks
    SwitchRoleService switchRoleService;

    @Test
    void testVerifyDriver() {
        DriverModel driver = new DriverModel();
        driver.setStatus("approved");

        when(driverRepo.findByEmail("test@sm.imamu.edu.sa")).thenReturn(driver);

        boolean result = switchRoleService.verifyDriver("test@sm.imamu.edu.sa");

        assertTrue(result);

        System.out.println("Functional Test: Approved driver verified successfully.");
    }

    @Test
    void testBecomeDriver() {
        DriverModel driver = new DriverModel();
        driver.setStatus("approved");

        when(driverRepo.findByEmail("test@sm.imamu.edu.sa")).thenReturn(driver);

        String result = switchRoleService.becomeDriver("test@sm.imamu.edu.sa");

        assertEquals("switch_to_driver", result);

        System.out.println("Functional Test: Approved driver can switch role.");
    }
}