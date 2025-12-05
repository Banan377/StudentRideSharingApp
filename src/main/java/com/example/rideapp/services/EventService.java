package com.example.rideapp.services;

import com.example.rideapp.models.EventModel;
import com.example.rideapp.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService {
    
    @Autowired
    private EventRepository eventRepository;
    
    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }
    
    public List<EventModel> getUpcomingEvents() {
        return eventRepository.findByEventDateAfter(LocalDateTime.now());
    }
    
    public EventModel getEventById(Long id) {
        return eventRepository.findById(id).orElse(null);
    }
    
    public EventModel createEvent(EventModel event) {
        return eventRepository.save(event);
    }
    
    public EventModel updateEvent(Long id, EventModel eventDetails) {
        EventModel event = eventRepository.findById(id).orElse(null);
        if (event != null) {
            event.setEventName(eventDetails.getEventName());
            event.setEventDate(eventDetails.getEventDate());
            event.setLocation(eventDetails.getLocation());
            event.setDescription(eventDetails.getDescription());
            return eventRepository.save(event);
        }
        return null;
    }
    
    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public List<EventModel> searchEvents(String keyword) {
        return eventRepository.findByEventNameContaining(keyword);
    }
}