package com.eventmaster.controller;

import com.eventmaster.model.Event;
import com.eventmaster.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventService eventService;

    // Create a new event
    @PostMapping("/save")
    public Event createEvent(@RequestBody Event event) {
        return eventService.createEvent(event);
    }

    // Retrieve event by ID
    @GetMapping("/{id}")
    public Optional<Event> getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    // Retrieve all events (with optional filters)
    @GetMapping
    public List<Event> getAllEvents(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String venue) {
        return eventService.getAllEvents(category, status, venue);
    }

    // Update an event
    @PutMapping("/edit/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        return eventService.updateEvent(id, updatedEvent);
    }

    // Delete an event
    @DeleteMapping("/delete/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}