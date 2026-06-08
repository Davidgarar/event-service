package co.vivaeventos.eventplatform.repository;

import co.vivaeventos.eventplatform.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // ✅ CORREGIDO: Cambiamos e.city por e.location para que coincida con tu clase Event
    @Query("SELECT e FROM Event e WHERE " +
           "(:city IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :city, '%'))) AND " +
           "(:startDate IS NULL OR e.eventDate >= :startDate) AND " +
           "(:endDate IS NULL OR e.eventDate <= :endDate)")
    List<Event> filterEvents(@Param("city") String city, 
                             @Param("startDate") LocalDateTime startDate, 
                             @Param("endDate") LocalDateTime endDate);
}