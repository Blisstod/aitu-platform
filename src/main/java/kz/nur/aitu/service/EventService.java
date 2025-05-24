package kz.nur.aitu.service;

import kz.nur.aitu.dto.EventCreateDto;
import kz.nur.aitu.dto.EventDto;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.Event;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.entity.Image;
import kz.nur.aitu.enums.Role;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.mapper.EventMapper;
import kz.nur.aitu.repository.ClubRepository;
import kz.nur.aitu.repository.EventRepository;
import kz.nur.aitu.repository.UserRepository;
import kz.nur.aitu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EventService {

    private final EventRepository eventRepo;
    private final UserRepository userRepo;
    private final ClubRepository clubRepository;
    private final EventMapper mapper;
    private final SecurityUtils securityUtils;
    private final ImageService imageService;

    public List<EventDto> getAllEvents() {
        return eventRepo.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public List<EventDto> getEventsByClub(UUID clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ResourceNotFoundException("Клуб не найден");
        }
        return eventRepo.findByClub_Id(clubId).stream()
                .map(mapper::toDto)
                .toList();
    }

    public List<EventDto> getGlobalEvents() {
        return eventRepo.findByClubIsNull().stream()
                .map(mapper::toDto)
                .toList();
    }

    public EventDto getEventById(UUID id) {
        Event event = eventRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found!"));
        return mapper.toDto(event);
    }

    @Transactional
    public EventDto createEvent(EventCreateDto dto) throws AccessDeniedException {
        User me = securityUtils.getCurrentUser();
        Event ev = Event.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .format(dto.getFormat())
                .address(dto.getAddress())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();

        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId())
                    .orElseThrow(() -> new ResourceNotFoundException("Клуб не найден"));
            if (!club.getAdmins().contains(me)) {
                throw new AccessDeniedException("Только админы клуба могут создавать его события");
            }
            ev.setClub(club);
            ev.setAdmins(new HashSet<>(club.getAdmins()));
        } else {
            // университетское событие
            if (me.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("Только университетские админы могут создавать глобальные события");
            }
            ev.setClub(null);
            ev.setAdmins(Collections.singleton(me));
        }

        if (dto.getImageIds() != null) {
            List<Image> images = dto.getImageIds().stream()
                    .map(imageService::getImageEntity)  // достаём картинку
                    .collect(Collectors.toList());
            ev.setImages(images);
        }

        return mapper.toDto(eventRepo.save(ev));
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

        if (dto.getImageIds() != null) {
            List<Image> images = dto.getImageIds().stream()
                    .map(imageService::getImageEntity)
                    .collect(Collectors.toList());
            e.setImages(images);
        }

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

    @Transactional
    public void addImageToEvent(UUID eventId, UUID imageId) {
        Event ev = eventRepo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Событие не найдено"));
        Image img = imageService.getImageEntity(imageId);
        ev.getImages().add(img);
        eventRepo.save(ev);
    }

//    @Transactional
//    public UUID addEventImage(UUID eventId, MultipartFile file) throws IOException {
//        Event ev = eventRepo.findById(eventId)
//                .orElseThrow(() -> new ResourceNotFoundException("Событие не найдено"));
//        UUID imageId = imageService.uploadImage(file);
//        ev.getImages().add(imageService.getImageEntity(imageId));
//        eventRepo.save(ev);
//        return imageId;
//    }

    public List<UUID> listEventImages(UUID eventId) {
        return eventRepo.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Событие не найдено"))
                .getImages().stream()
                .map(Image::getId)
                .toList();
    }

//    public byte[] downloadEventImage(UUID eventId, UUID imageId) throws IOException, DataFormatException {
//        Event ev = eventRepo.findById(eventId)
//                .orElseThrow(() -> new ResourceNotFoundException("Событие не найдено"));
//        boolean contains = ev.getImages().stream()
//                .anyMatch(img -> img.getId().equals(imageId));
//        if (!contains) {
//            throw new ResourceNotFoundException("Фото не принадлежит этому событию");
//        }
//        return imageService.downloadImage(imageId);
//    }
}
