package co.vivaeventos.eventplatform.delivery.rest;

import co.vivaeventos.eventplatform.model.Event;
import co.vivaeventos.eventplatform.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        return eventService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Event>> searchEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) Boolean onlyAvailable) {
        
        List<Event> events = eventService.findAll();
        
        // Filtrar por ciudad
        if (city != null && !city.isEmpty()) {
            events = events.stream()
                    .filter(e -> e.getLocation().toLowerCase().contains(city.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Filtrar por fecha (solo eventos futuros)
        if (fromDate != null) {
            events = events.stream()
                    .filter(e -> e.getEventDate().isAfter(fromDate))
                    .collect(Collectors.toList());
        }
        
        if (toDate != null) {
            events = events.stream()
                    .filter(e -> e.getEventDate().isBefore(toDate))
                    .collect(Collectors.toList());
        }
        
        // Filtrar solo disponibles (con cupo > 0)
        if (onlyAvailable != null && onlyAvailable) {
            events = events.stream()
                    .filter(e -> e.getAvailableCapacity() > 0)
                    .collect(Collectors.toList());
        }
        
        // Ordenar por fecha (próximos primero)
        events.sort(Comparator.comparing(Event::getEventDate));
        
        return ResponseEntity.ok(events);
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        Event savedEvent = eventService.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEvent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @Valid @RequestBody Event event) {
        if (!eventService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        event.setId(id);
        Event updatedEvent = eventService.save(event);
        return ResponseEntity.ok(updatedEvent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        if (!eventService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reserve")
    public ResponseEntity<Event> reserveTickets(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {

        Event updatedEvent = eventService.reserveTickets(id, quantity);

        return ResponseEntity.ok(updatedEvent);
    }
}