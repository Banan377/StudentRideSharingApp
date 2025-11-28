package com.example.rideapp.service;

import com.example.rideapp.models.EventModel;
import com.example.rideapp.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<EventModel> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public EventModel createEvent(EventModel event) {
        return eventRepository.save(event);
    }

    public EventModel updateEvent(Long id, EventModel eventDetails) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        
        event.setEventName(eventDetails.getEventName());
        event.setEventDate(eventDetails.getEventDate());
        event.setLocation(eventDetails.getLocation());
        event.setDescription(eventDetails.getDescription());
        
        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        EventModel event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        eventRepository.delete(event);
    }
}