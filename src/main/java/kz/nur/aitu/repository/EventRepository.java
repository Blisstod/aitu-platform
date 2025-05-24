package kz.nur.aitu.repository;

import kz.nur.aitu.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByClub_Id(UUID clubId);
    List<Event> findByClubIsNull(); // глобальные события
}
