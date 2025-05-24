package kz.nur.aitu.service;

import kz.nur.aitu.dto.EventCreateDto;
import kz.nur.aitu.dto.EventDto;
import kz.nur.aitu.entity.Event;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.mapper.EventMapper;
import kz.nur.aitu.repository.EventRepository;
import kz.nur.aitu.repository.UserRepository;
import kz.nur.aitu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final EventMapper mapper;
    private final SecurityUtils securityUtils;

    public List<EventDto> getAllEvents() {
        return eventRepo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public EventDto getEventById(UUID id) {
        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found!"));
        return mapper.toDto(event);
    }

    @Transactional
    public EventDto createEvent(EventCreateDto dto) {
        Event e = mapper.toEntity(dto);
        applyParticipantsAndAdmins(e, dto);
        User me = securityUtils.getCurrentUser();
        e.getAdmins().add(me);
        Event saved = eventRepo.save(e);
        return mapper.toDto(saved);
    }

    @Transactional
    public EventDto updateEvent(UUID id, EventCreateDto dto) {
        Event e = eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found!"));

        // Обновляем поля
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setFormat(dto.getFormat());
        e.setAddress(dto.getAddress());
        e.setStartDate(dto.getStartDate());
        e.setEndDate(dto.getEndDate());
        applyParticipantsAndAdmins(e, dto);

        Event updated = eventRepo.save(e);
        return mapper.toDto(updated);
    }

    @Transactional
    public void subscribeEvent(UUID eventId) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found during subscribing!"));
        User me = securityUtils.getCurrentUser();
        e.getParticipants().add(me);
        eventRepo.save(e);
    }

    @Transactional
    public void unsubscribeEvent(UUID eventId) {
        Event e = eventRepo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found during unsubscribing!"));
        User me = securityUtils.getCurrentUser();
        e.getParticipants().remove(me);
        eventRepo.save(e);
    }

    private void applyParticipantsAndAdmins(Event e, EventCreateDto dto) {
        if (dto.getParticipantIds() != null) {
            Set<User> parts = new HashSet<>(userRepo.findAllById(dto.getParticipantIds()));
            e.setParticipants(parts);
        }
        if (dto.getAdminIds() != null) {
            Set<User> admins = new HashSet<>(userRepo.findAllById(dto.getAdminIds()));
            e.setAdmins(admins);
        }
    }
}
