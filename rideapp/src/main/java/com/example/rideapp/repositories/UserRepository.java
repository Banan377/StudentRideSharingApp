package com.example.rideapp.repositories;

import com.example.rideapp.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel findByEmail(String email);
}
