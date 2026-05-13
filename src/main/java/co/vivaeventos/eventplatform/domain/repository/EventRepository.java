package co.vivaeventos.eventplatform.domain.repository;

import co.vivaeventos.eventplatform.domain.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}