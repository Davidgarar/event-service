package co.vivaeventos.eventplatform.service;

import co.vivaeventos.eventplatform.dto.TicketTypeRequest;
import co.vivaeventos.eventplatform.dto.TicketTypeResponse;
import co.vivaeventos.eventplatform.model.Event;
import co.vivaeventos.eventplatform.model.TicketType;
import co.vivaeventos.eventplatform.repository.EventRepository;
import co.vivaeventos.eventplatform.repository.TicketTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketTypeService {

    private final TicketTypeRepository ticketTypeRepository;
    private final EventRepository eventRepository;

    @Transactional
    public TicketTypeResponse addTicketType(Long eventId, TicketTypeRequest request) {
        log.info("Agregando tipo de boleta para evento {}: {}", eventId, request.getName());

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Validar que no exceda el cupo total
        List<TicketType> existingTypes = ticketTypeRepository.findByEventId(eventId);
        int totalExistingCapacity = existingTypes.stream()
                .mapToInt(TicketType::getCapacity)
                .sum();

        if (totalExistingCapacity + request.getCapacity() > event.getTotalCapacity()) {
            throw new RuntimeException("El cupo total excede la capacidad del evento");
        }

        TicketType ticketType = new TicketType();
        ticketType.setEventId(eventId);
        ticketType.setName(request.getName().toUpperCase());
        ticketType.setPrice(request.getPrice());
        ticketType.setCapacity(request.getCapacity());
        ticketType.setAvailableCapacity(request.getCapacity());

        TicketType saved = ticketTypeRepository.save(ticketType);
        log.info("Tipo de boleta agregado: {}", saved.getName());

        return new TicketTypeResponse(
            saved.getId(),
            saved.getName(),
            saved.getPrice(),
            saved.getCapacity(),
            saved.getAvailableCapacity()
        );
    }

    public List<TicketTypeResponse> getTicketTypesByEvent(Long eventId) {
        return ticketTypeRepository.findByEventId(eventId)
                .stream()
                .map(tt -> new TicketTypeResponse(
                    tt.getId(),
                    tt.getName(),
                    tt.getPrice(),
                    tt.getCapacity(),
                    tt.getAvailableCapacity()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public TicketType reserveTickets(Long eventId, String ticketTypeName, Integer quantity) {
        log.info("Reservando {} boletas tipo {} para evento {}", quantity, ticketTypeName, eventId);

        List<TicketType> ticketTypes = ticketTypeRepository.findByEventId(eventId);
        
        TicketType ticketType = ticketTypes.stream()
                .filter(tt -> tt.getName().equalsIgnoreCase(ticketTypeName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tipo de boleta no encontrado"));

        if (ticketType.getAvailableCapacity() < quantity) {
            throw new RuntimeException("No hay suficientes boletas disponibles para el tipo " + ticketTypeName);
        }

        ticketType.setAvailableCapacity(ticketType.getAvailableCapacity() - quantity);
        TicketType updated = ticketTypeRepository.save(ticketType);
        
        log.info("Reserva exitosa. Disponible ahora: {}", updated.getAvailableCapacity());
        
        return updated;  // ← Cambiado: ahora retorna TicketType
    }
    public TicketType findByEventIdAndName(Long eventId, String name) {
        List<TicketType> types = ticketTypeRepository.findByEventId(eventId);
        return types.stream()
                .filter(tt -> tt.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Tipo de boleta no encontrado: " + name));
    }
}