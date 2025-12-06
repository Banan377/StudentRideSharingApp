package com.example.rideapp.repositories;

import com.example.rideapp.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, String> {
    boolean existsByEmail(String email);
    Optional<Student> findByEmail(String email);  
}