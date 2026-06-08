error id: file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java:java/lang/String#
file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java
empty definition using pc, found symbol in pc: java/lang/String#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 721
uri: file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventServiceImpl.java
text:
```scala
@Override
@Transactional
public Event updateEventPartial(Long id, java.util.Map<String, Object> updates) {
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
        event.setEventDate(java.time.LocalDateTime.parse(dateStr));
    }
    if (updates.containsKey("location")) {
        event.setLocation((St@@ring) updates.get("location"));
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
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/lang/String#