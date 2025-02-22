package kz.nur.aitu.repository;

import kz.nur.aitu.entity.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ClubRepository extends JpaRepository<Club, UUID> {
    Optional<Club> findByName(String name);
}
