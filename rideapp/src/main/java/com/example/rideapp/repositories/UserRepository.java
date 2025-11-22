package com.example.rideapp.repositories;

import com.example.rideapp.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, String> {
    UserModel findByEmail(String email);
    boolean existsByEmail(String email);    
    Optional<UserModel> findById(String email);
}
