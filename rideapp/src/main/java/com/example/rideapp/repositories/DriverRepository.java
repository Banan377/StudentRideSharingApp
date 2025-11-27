package com.example.rideapp.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rideapp.models.DriverModel;

@Repository
public interface DriverRepository extends JpaRepository<DriverModel, String> {

    boolean existsByEmail(String email);

    DriverModel findByEmail(String email);

    List<DriverModel> findByStatus(String status);

    void deleteByEmail(String email);

}
