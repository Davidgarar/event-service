package co.vivaeventos.eventplatform.controller;

import co.vivaeventos.eventplatform.dto.TicketTypeRequest;
import co.vivaeventos.eventplatform.dto.TicketTypeResponse;
import co.vivaeventos.eventplatform.model.TicketType;
import co.vivaeventos.eventplatform.service.TicketTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/events/{eventId}/ticket-types")
@RequiredArgsConstructor
public class TicketTypeController {

    private final TicketTypeService ticketTypeService;

    // ✅ NUEVO MÉTODO - Crear tipo de boleta
    @PostMapping
    public ResponseEntity<TicketTypeResponse> addTicketType(
            @PathVariable Long eventId,
            @Valid @RequestBody TicketTypeRequest request) {
        TicketTypeResponse response = ticketTypeService.addTicketType(eventId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Reservar tipo de boleta
    @PostMapping("/{ticketTypeName}/reserve")
    public ResponseEntity<Map<String, Object>> reserveTicketType(
            @PathVariable Long eventId,
            @PathVariable String ticketTypeName,
            @RequestParam Integer quantity) {
        
        TicketType ticketType = ticketTypeService.reserveTickets(eventId, ticketTypeName, quantity);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("ticketType", ticketType.getName());
        response.put("remainingCapacity", ticketType.getAvailableCapacity());
        response.put("price", ticketType.getPrice());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{ticketTypeName}/availability")
    public ResponseEntity<Map<String, Object>> checkAvailability(
            @PathVariable Long eventId,
            @PathVariable String ticketTypeName,
            @RequestParam Integer quantity) {
        
        TicketType ticketType = ticketTypeService.findByEventIdAndName(eventId, ticketTypeName);
        
        Map<String, Object> response = new HashMap<>();
        response.put("available", ticketType.getAvailableCapacity() >= quantity);
        response.put("availableCapacity", ticketType.getAvailableCapacity());
        response.put("price", ticketType.getPrice());
        response.put("eventId", eventId);
        response.put("ticketType", ticketTypeName);
        
        return ResponseEntity.ok(response);
    }

    // Listar tipos de boleta
    @GetMapping
    public ResponseEntity<List<TicketTypeResponse>> getTicketTypes(@PathVariable Long eventId) {
        return ResponseEntity.ok(ticketTypeService.getTicketTypesByEvent(eventId));
    }
}