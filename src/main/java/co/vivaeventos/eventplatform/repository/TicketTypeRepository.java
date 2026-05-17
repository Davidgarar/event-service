package co.vivaeventos.eventplatform.repository;

import co.vivaeventos.eventplatform.model.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {
    List<TicketType> findByEventId(Long eventId);
}