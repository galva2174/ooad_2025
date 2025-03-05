package com.eventmaster.repository;

import com.eventmaster.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByCategory(String category); // Filter by category
    List<Event> findByStatus(String status); // Filter by status
    List<Event> findByVenue(String venue); // Filter by venue
}