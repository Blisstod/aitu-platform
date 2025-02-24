package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import kz.nur.aitu.dto.*;
import kz.nur.aitu.enums.ResponseStatus;
import kz.nur.aitu.service.ClubApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/applications")
@RequiredArgsConstructor
@Tag(name = "Club Application API", description = "API для управления заявками в клуб")
public class ClubApplicationController {

    private final ClubApplicationService service;

    @Operation(summary = "Создать новую форму заявки")
    @PostMapping("/form")
    public ResponseEntity<ClubApplicationFormDto> createForm(@RequestBody ClubApplicationFormCreateDto dto) {
        return ResponseEntity.ok(service.createForm(dto));
    }

    @Operation(summary = "Получить форму по ID")
    @GetMapping("/form/{id}")
    public ResponseEntity<ClubApplicationFormDto> getFormById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getFormById(id));
    }

    @Operation(summary = "Get all forms by club id")
    @GetMapping("/form/club/{clubId}")
    public ResponseEntity<List<ClubApplicationFormDto>> getFormsByClubId(@PathVariable UUID clubId) {
        return ResponseEntity.ok(service.getAllFormsByClubId(clubId));
    }

    @Operation(summary = "Получить заявки пользователя")
    @GetMapping("/form/visitor")
    public ResponseEntity<List<ClubApplicationFormDto>> getFormsByVisitor() {
        return ResponseEntity.ok(service.getFormsByVisitor());
    }

    @Operation(summary = "Получить список всех заявок по ID формы")
    @GetMapping("/requests/{formId}")
    public ResponseEntity<List<ClubApplicationRequestDto>> getRequestsByFormId(@PathVariable UUID formId) {
        return ResponseEntity.ok(service.getRequestsByFormId(formId));
    }

    @Operation(summary = "Студент отправляет заявку на вступление в клуб")
    @PostMapping("/request")
    public ResponseEntity<ClubApplicationRequestDto> createRequest(@RequestBody ClubApplicationRequestCreateDto dto) {
        return ResponseEntity.ok(service.createRequest(dto));
    }

    @Operation(summary = "Получить запрос по ID")
    @GetMapping("/request/{id}")
    public ResponseEntity<ClubApplicationRequestDto> getRequestById(@PathVariable UUID id) {
        return ResponseEntity.ok(service.getRequestId(id));
    }

    @Operation(summary = "Получить заявки пользователя")
    @GetMapping("/request/visitor")
    public ResponseEntity<List<ClubApplicationRequestDto>> getRequestsByVisitor() {
        return ResponseEntity.ok(service.getRequestsByVisitor());
    }

    @Operation(summary = "Администратор отвечает на заявку студента")
    @PostMapping("/request/response")
    public ResponseEntity<ClubApplicationRequestDto> respondToRequest(@RequestBody ClubApplicationRequestResponse dto) {
        return ResponseEntity.ok(service.respondToRequest(dto));
    }


}
