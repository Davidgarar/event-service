package co.vivaeventos.eventplatform.domain.service;


import co.vivaeventos.eventplatform.domain.model.Event;
import co.vivaeventos.eventplatform.domain.repository.EventRepository;

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

        Event event = new Event(
                "Rock Fest",
                "Concierto",
                LocalDateTime.now(),
                "Bogotá",
                100
        );

        when(eventRepository.findById(1L))
                .thenReturn(Optional.of(event));

        when(eventRepository.save(any(Event.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Event updatedEvent = eventService.reserveTickets(1L, 2);

        assertEquals(98, updatedEvent.getAvailableCapacity());
    }

    @Test
    void shouldThrowExceptionWhenNoCapacity() {

        Event event = new Event(
                "Rock Fest",
                "Concierto",
                LocalDateTime.now(),
                "Bogotá",
                1
        );

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