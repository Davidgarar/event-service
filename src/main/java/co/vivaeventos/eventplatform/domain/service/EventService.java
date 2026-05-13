package co.vivaeventos.eventplatform.domain.service;

import co.vivaeventos.eventplatform.domain.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventService {

    Event save(Event event);

    Optional<Event> findById(Long id);

    List<Event> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean hasAvailableCapacity(Long eventId, Integer quantity);

    Event reserveTickets(Long eventId, Integer quantity);
}