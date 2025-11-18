package com.example.rideapp.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.rideapp.models.Student;
import com.example.rideapp.repositories.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Student> isValidStudentEmail(String email) {
        // يتحقق إذا الإيميل موجود في قاعدة الجامعة
        return studentRepository.findByEmail(email);
    }
}
