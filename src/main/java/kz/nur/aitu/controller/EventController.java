package kz.nur.aitu.controller;

import kz.nur.aitu.dto.EventCreateDto;
import kz.nur.aitu.dto.EventDto;
import kz.nur.aitu.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/events")
@RequiredArgsConstructor
@Validated
public class EventController {

    private final EventService eventService;

    /** ВСЕ события (доступно всем аутентифицированным пользователям) */
    @GetMapping
    public List<EventDto> getAll() {
        return eventService.getAllEvents();
    }

    /** Конкретное событие по ID */
    @GetMapping("/{id}")
    public EventDto getById(@PathVariable UUID id) {
        return eventService.getEventById(id);
    }

    /** Создание (только ADMIN) */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EventDto> create(@RequestBody @Validated EventCreateDto dto) {
        EventDto created = eventService.createEvent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /** Редактирование (только ADMIN) */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public EventDto update(@PathVariable UUID id,
                           @RequestBody @Validated EventCreateDto dto) {
        return eventService.updateEvent(id, dto);
    }

    @PostMapping("/{id}/subscribe")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> subscribe(@PathVariable UUID id) {
        eventService.subscribeEvent(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/unsubscribe")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Void> unsubscribe(@PathVariable UUID id) {
        eventService.unsubscribeEvent(id);
        return ResponseEntity.ok().build();
    }
}
