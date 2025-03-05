package com.eventmaster.service;

import com.eventmaster.model.Event;
import com.eventmaster.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Create a new event
    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    // Retrieve event by ID
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    // Retrieve all events (with optional filters)
    public List<Event> getAllEvents(String category, String status, String venue) {
        if (category != null) {
            return eventRepository.findByCategory(category);
        } else if (status != null) {
            return eventRepository.findByStatus(status);
        } else if (venue != null) {
            return eventRepository.findByVenue(venue);
        } else {
            return eventRepository.findAll();
        }
    }

    // Update an event
    public Event updateEvent(Long id, Event updatedEvent) {
        Optional<Event> existingEvent = eventRepository.findById(id);
        if (existingEvent.isPresent()) {
            Event event = existingEvent.get();
            event.setTitle(updatedEvent.getTitle());
            event.setCategory(updatedEvent.getCategory());
            event.setDate(updatedEvent.getDate());
            event.setTime(updatedEvent.getTime());
            event.setVenue(updatedEvent.getVenue());
            event.setStatus(updatedEvent.getStatus());
            event.setMaxAttendees(updatedEvent.getMaxAttendees());
            event.setCurrentAttendees(updatedEvent.getCurrentAttendees());
            return eventRepository.save(event);
        } else {
            throw new RuntimeException("Event not found with id: " + id);
        }
    }

    // Delete an event
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}