package com.example.rideapp.controller;

import com.example.rideapp.models.EventModel;
import com.example.rideapp.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Get all events - تم التصحيح
    @GetMapping
    public List<EventModel> getAllEvents() {
        return eventService.getAllEvents();
    }

    // Get event by ID - تم التصحيح
    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable Long id) {
        Optional<EventModel> event = eventService.getEventById(id);
        return event.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
    }

    // Create new event
    @PostMapping
    public EventModel createEvent(@RequestBody EventModel event) {
        return eventService.createEvent(event);
    }

    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(@PathVariable Long id, @RequestBody EventModel eventDetails) {
        try {
            EventModel updatedEvent = eventService.updateEvent(id, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        try {
            eventService.deleteEvent(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}