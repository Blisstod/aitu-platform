package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nur.aitu.dto.ClubDto;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.DataFormatException;

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

    @Operation(summary = "Получить всех участников клуба")
    @GetMapping("/{clubId}/members")
    public ResponseEntity<List<UserDto>> getAllMembers(@PathVariable UUID clubId) {
        return ResponseEntity.ok(clubService.getAllMembers(clubId));
    }

    @Operation(summary = "Получить всех админов клуба")
    @GetMapping("/{clubId}/admins")
    public ResponseEntity<List<UserDto>> getAllAdmins(@PathVariable UUID clubId) {
        return ResponseEntity.ok(clubService.getAllAdmins(clubId));
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

    @GetMapping("/{id}/images")
    public ResponseEntity<List<UUID>> listImages(@PathVariable UUID id) {
        return ResponseEntity.ok(clubService.listClubImages(id));
    }

    @PostMapping("/{clubId}/images/{imageId}")
    @Operation(summary = "Привязать существующую картинку к клубу")
    public ResponseEntity<Void> addImage(
            @PathVariable UUID clubId,
            @PathVariable UUID imageId
    ) {
        clubService.addImageToClub(clubId, imageId);
        return ResponseEntity.ok().build();
    }
}
