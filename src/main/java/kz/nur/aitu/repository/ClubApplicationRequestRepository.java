package kz.nur.aitu.repository;

import kz.nur.aitu.entity.ClubApplicationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ClubApplicationRequestRepository extends JpaRepository<ClubApplicationRequest, UUID> {
    List<ClubApplicationRequest> findByClubApplicationFormId(UUID formId);
    List<ClubApplicationRequest> findByCreatedBy(String createdBy);
}
