package com.example.rideapp.controllers;

import com.example.rideapp.models.EventModel;
import com.example.rideapp.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Get all events
    @GetMapping
    public ResponseEntity<List<EventModel>> getAllEvents() {
        List<EventModel> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    // Get upcoming events only
    @GetMapping("/upcoming")
    public ResponseEntity<List<EventModel>> getUpcomingEvents() {
        List<EventModel> events = eventService.getUpcomingEvents();
        return ResponseEntity.ok(events);
    }

    // Get event by ID
    @GetMapping("/{id}")
    public ResponseEntity<EventModel> getEventById(@PathVariable Long id) {
        EventModel event = eventService.getEventById(id);
        return event != null ? 
               ResponseEntity.ok(event) : 
               ResponseEntity.notFound().build();
    }

    // Create new event
    @PostMapping
    public ResponseEntity<EventModel> createEvent(@RequestBody EventModel event) {
        EventModel createdEvent = eventService.createEvent(event);
        return ResponseEntity.ok(createdEvent);
    }

    // Update event
    @PutMapping("/{id}")
    public ResponseEntity<EventModel> updateEvent(@PathVariable Long id, @RequestBody EventModel eventDetails) {
        EventModel updatedEvent = eventService.updateEvent(id, eventDetails);
        return updatedEvent != null ? 
               ResponseEntity.ok(updatedEvent) : 
               ResponseEntity.notFound().build();
    }

    // Delete event
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        return deleted ? 
               ResponseEntity.ok().build() : 
               ResponseEntity.notFound().build();
    }

    // Search events
    @GetMapping("/search")
    public ResponseEntity<List<EventModel>> searchEvents(@RequestParam String keyword) {
        List<EventModel> events = eventService.searchEvents(keyword);
        return ResponseEntity.ok(events);
    }
}