package co.vivaeventos.eventplatform.delivery.rest;

import co.vivaeventos.eventplatform.domain.model.Event;
import co.vivaeventos.eventplatform.domain.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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