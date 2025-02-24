package kz.nur.aitu.repository;

import kz.nur.aitu.entity.ClubApplicationForm;
import kz.nur.aitu.entity.ClubApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClubApplicationFormRepository extends JpaRepository<ClubApplicationForm, UUID> {
    List<ClubApplicationForm> findByClubId(UUID clubId);
    List<ClubApplicationForm> findByCreatedBy(String createdBy);
    List<ClubApplicationForm> findByClubIdOrderByCreatedAtDesc(UUID clubId);
}

