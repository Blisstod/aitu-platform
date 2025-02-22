package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nur.aitu.dto.ClubDto;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/rest/clubs")
@RequiredArgsConstructor
@Tag(name = "Клубы", description = "Контроллер для управления клубами")
public class ClubController {

    private final ClubService clubService;

    @Operation(summary = "Получить список всех клубов")
    @GetMapping
    public ResponseEntity<List<ClubDto>> getAllClubs() {
        return ResponseEntity.ok(clubService.getAllClubs());
    }

    @Operation(summary = "Получить клуб по ID")
    @GetMapping("/{id}")
    public ResponseEntity<ClubDto> getClubById(@PathVariable UUID id) {
        Optional<ClubDto> club = clubService.getClubById(id);
        return club.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Создать новый клуб")
    @PostMapping
    public ResponseEntity<ClubDto> createClub(@RequestBody ClubDto clubDto) {
        return ResponseEntity.ok(clubService.createClub(clubDto));
    }

    @Operation(summary = "Удалить клуб по ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable UUID id) {
        clubService.deleteClub(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Получить всех участников клуба, включая администраторов")
    @GetMapping("/{clubId}/members")
    public ResponseEntity<List<UserDto>> getAllMembers(@PathVariable UUID clubId) {
        return ResponseEntity.ok(clubService.getAllMembers(clubId));
    }

    @Operation(summary = "Добавить администратора в клуб")
    @PostMapping("/{clubId}/admins/{adminId}")
    public ResponseEntity<ClubDto> addAdmin(@PathVariable UUID clubId, @PathVariable Long adminId) {
        return ResponseEntity.ok(clubService.addAdminToClub(clubId, adminId));
    }

    @Operation(summary = "Удалить администратора из клуба")
    @DeleteMapping("/{clubId}/admins/{adminId}")
    public ResponseEntity<ClubDto> removeAdmin(@PathVariable UUID clubId, @PathVariable Long adminId) {
        return ResponseEntity.ok(clubService.removeAdminFromClub(clubId, adminId));
    }

    @Operation(summary = "Добавить участника в клуб")
    @PostMapping("/{clubId}/members/{memberId}")
    public ResponseEntity<ClubDto> addMember(@PathVariable UUID clubId, @PathVariable Long memberId) {
        return ResponseEntity.ok(clubService.addMemberToClub(clubId, memberId));
    }

    @Operation(summary = "Удалить участника из клуба")
    @DeleteMapping("/{clubId}/members/{memberId}")
    public ResponseEntity<ClubDto> removeMember(@PathVariable UUID clubId, @PathVariable Long memberId) {
        return ResponseEntity.ok(clubService.removeMemberFromClub(clubId, memberId));
    }
}
