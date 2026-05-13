package co.vivaeventos.eventplatform.domain.service;

import co.vivaeventos.eventplatform.domain.model.Event;
import co.vivaeventos.eventplatform.domain.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
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

        if(event.getAvailableCapacity() < quantity){
            throw new RuntimeException("No hay suficientes cupos disponibles");
        }

        event.setAvailableCapacity(
                event.getAvailableCapacity() - quantity
        );

        return eventRepository.save(event);
    }
}