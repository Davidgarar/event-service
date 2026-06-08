package co.vivaeventos.eventplatform.service;

import co.vivaeventos.eventplatform.model.Event;
import co.vivaeventos.eventplatform.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        return eventRepository.save(event);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Event> findById(Long id) {
        return eventRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return eventRepository.existsById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasAvailableCapacity(Long eventId, Integer quantity) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));
        return event.getAvailableCapacity() >= quantity;
    }

    @Override
    @Transactional
    public Event reserveTickets(Long eventId, Integer quantity) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (event.getAvailableCapacity() < quantity) {
            throw new RuntimeException("No hay suficientes cupos disponibles");
        }

        event.setAvailableCapacity(event.getAvailableCapacity() - quantity);
        return eventRepository.save(event);
    }

    @Override
    @Transactional
    public Event updateEventPartial(Long id, Map<String, Object> updates) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        if (updates.containsKey("name")) {
            event.setName((String) updates.get("name"));
        }
        if (updates.containsKey("description")) {
            event.setDescription((String) updates.get("description"));
        }
        if (updates.containsKey("eventDate")) {
            String dateStr = (String) updates.get("eventDate");
            event.setEventDate(LocalDateTime.parse(dateStr));
        }
        if (updates.containsKey("location")) {
            event.setLocation((String) updates.get("location"));
        }
        if (updates.containsKey("price")) {
            Double newPrice = ((Number) updates.get("price")).doubleValue();
            event.setPrice(newPrice);
        }
        if (updates.containsKey("totalCapacity")) {
            Integer newCapacity = ((Number) updates.get("totalCapacity")).intValue();
            event.setTotalCapacity(newCapacity);
            if (event.getAvailableCapacity() > newCapacity) {
                event.setAvailableCapacity(newCapacity);
            }
        }

        return eventRepository.save(event);
    }
}