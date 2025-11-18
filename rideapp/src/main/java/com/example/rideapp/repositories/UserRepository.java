package com.example.rideapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rideapp.models.UserModel;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    boolean existsByEmail(String email);
}
