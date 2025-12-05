package com.example.rideapp.repositories;

import com.example.rideapp.models.EventModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventModel, Long> {
    List<EventModel> findByEventDateAfter(LocalDateTime date);
    List<EventModel> findByLocationContaining(String location);
    List<EventModel> findByEventNameContaining(String eventName);
}