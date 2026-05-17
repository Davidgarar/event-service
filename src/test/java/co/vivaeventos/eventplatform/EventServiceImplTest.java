package co.vivaeventos.eventplatform;

import co.vivaeventos.eventplatform.model.Event;
import co.vivaeventos.eventplatform.repository.EventRepository;
import co.vivaeventos.eventplatform.service.EventServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void shouldReserveTicketsSuccessfully() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Rock Fest");
        event.setDescription("Concierto");
        event.setEventDate(LocalDateTime.now().plusDays(30));
        event.setLocation("Bogotá");
        event.setTotalCapacity(100);
        event.setAvailableCapacity(100);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Event updatedEvent = eventService.reserveTickets(1L, 2);

        assertEquals(98, updatedEvent.getAvailableCapacity());
    }

    @Test
    void shouldThrowExceptionWhenNoCapacity() {
        Event event = new Event();
        event.setId(1L);
        event.setName("Rock Fest");
        event.setDescription("Concierto");
        event.setEventDate(LocalDateTime.now().plusDays(30));
        event.setLocation("Bogotá");
        event.setTotalCapacity(1);
        event.setAvailableCapacity(1);

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> eventService.reserveTickets(1L, 5)
        );

        assertEquals(
                "No hay suficientes cupos disponibles",
                exception.getMessage()
        );
    }
}