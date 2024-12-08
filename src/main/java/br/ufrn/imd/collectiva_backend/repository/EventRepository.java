package br.ufrn.imd.collectiva_backend.repository;

import br.ufrn.imd.collectiva_backend.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EventRepository extends GenericRepository<Event> {

    @Query("""
            SELECT e FROM Event e
            WHERE (:queryEvent IS NULL OR :queryEvent = '' OR (e.name ILIKE CONCAT('%', :queryEvent, '%') OR CAST(e.id AS string) = :queryEvent))
            AND (:name IS NULL OR e.name = :name)
            AND (:location IS NULL OR e.location = :location)
            AND (:category IS NULL OR e.category = :category)
            AND (:description IS NULL OR e.description = :description)
            AND (
                (cast(:startDate as date) IS NULL OR e.startDate >= :startDate)
                AND (cast(:endDate as date) IS NULL OR e.endDate <= :endDate)
            )
            """)
    Page<Event> filterEventsByParams(String queryEvent, String name, String location, String category, LocalDateTime startDate,
                                     LocalDateTime endDate, String description, Pageable pageable);
}
