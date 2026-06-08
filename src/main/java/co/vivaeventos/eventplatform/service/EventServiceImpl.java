package co.vivaeventos.eventplatform.service;

import co.vivaeventos.eventplatform.model.Event;
import co.vivaeventos.eventplatform.repository.EventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

        if (event.getAvailableCapacity() < quantity) {
            throw new RuntimeException("No hay suficientes cupos disponibles");
        }

        event.setAvailableCapacity(event.getAvailableCapacity() - quantity);
        return eventRepository.save(event);
    }

    // ✅ NUEVO MÉTODO - Implementación de la lógica del filtro
    @Override
    @Transactional(readOnly = true)
    public List<Event> getFilteredEvents(String city, LocalDate date) {
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        if (date != null) {
            startDate = date.atStartOfDay(); // 00:00:00 del día solicitado
            endDate = date.atTime(23, 59, 59); // 23:59:59 del día solicitado
        }

        String cityParam = (city != null && !city.trim().isEmpty()) ? city.trim() : null;

        return eventRepository.filterEvents(cityParam, startDate, endDate);
    }
}