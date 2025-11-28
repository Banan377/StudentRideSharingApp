package com.example.rideapp.repositories;

import com.example.rideapp.models.EventModel; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {
}