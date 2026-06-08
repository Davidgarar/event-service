error id: file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventService.java:java/lang/Long#
file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventService.java
empty definition using pc, found symbol in pc: java/lang/Long#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 446
uri: file:///C:/Users/Digital/event-service/src/main/java/co/vivaeventos/eventplatform/service/EventService.java
text:
```scala
package co.vivaeventos.eventplatform.service;

import co.vivaeventos.eventplatform.model.Event;
import java.util.List;
import java.util.Optional;

public interface EventService {

    Event save(Event event);
    Optional<Event> findById(Long id);
    List<Event> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean hasAvailableCapacity(Long eventId, Integer quantity);
    Event reserveTickets(Lon@@g eventId, Integer quantity);
    
    // NUEVO MÉTODO
    Event updateEventPartial(Long id, java.util.Map<String, Object> updates);
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: java/lang/Long#