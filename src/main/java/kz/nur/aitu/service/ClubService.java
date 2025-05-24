package kz.nur.aitu.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nur.aitu.dto.ClubApplicationFormDto;
import kz.nur.aitu.dto.ClubDto;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.entity.Image;
import kz.nur.aitu.enums.ClubStatus;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.mapper.ClubApplicationMapper;
import kz.nur.aitu.mapper.ClubMapper;
import kz.nur.aitu.mapper.UserMapper;
import kz.nur.aitu.repository.ClubApplicationFormRepository;
import kz.nur.aitu.repository.ClubRepository;
import kz.nur.aitu.repository.UserRepository;
import kz.nur.aitu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
@Tag(name = "Club Service", description = "Сервис для управления клубами")
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubApplicationFormRepository formRepository;
    @Autowired
    private ClubMapper clubMapper;
    @Autowired
    private ClubApplicationMapper applicationMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtils securityUtils;
    private final ImageService imageService;

    @Operation(summary = "Получить список всех клубов")
    public List<ClubDto> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(clubMapper::toDto)
                .toList();
    }

    @Operation(summary = "Получить клуб по ID")
    public Optional<ClubDto> getClubById(UUID id) {
        return clubRepository.findById(id).map(club -> {
            ClubDto clubDto = clubMapper.toDto(club);

            // Получаем формы, отсортированные по createdAt DESC
            List<ClubApplicationFormDto> sortedForms = formRepository.findByClubIdOrderByCreatedAtDesc(id)
                    .stream()
                    .map(applicationMapper::toDto)
                    .toList();

            clubDto.setForms(sortedForms);
            return clubDto;
        });
    }

    @Operation(summary = "Создать новый клуб")
    public ClubDto createClub(ClubDto clubDto) {
        Club club = clubMapper.toEntity(clubDto);

        if (clubDto.getStatus() == null || clubDto.getStatus().isBlank()) {
            club.setStatus(ClubStatus.ACTIVE);
        }

        if (clubDto.getImageIds() != null) {
            List<Image> imgs = clubDto.getImageIds().stream()
                    .map(imageService::getImageEntity)
                    .collect(Collectors.toList());
            club.setImages(imgs);
        }

        club.setAdmins(Collections.singletonList(securityUtils.getCurrentUser()));

        return clubMapper.toDto(clubRepository.save(club));
    }

    @Operation(summary = "Удалить клуб по ID")
    public void deleteClub(UUID id) {
        clubRepository.deleteById(id);
    }

    @Operation(summary = "Добавить администратора в клуб")
    @Transactional
    public ClubDto addAdminToClub(UUID clubId, Long adminId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!club.getAdmins().contains(user)) {
            club.getAdmins().add(user);
            clubRepository.save(club);
        }
        return clubMapper.toDto(club);
    }

    @Operation(summary = "Удалить администратора из клуба")
    @Transactional
    public ClubDto removeAdminFromClub(UUID clubId, Long adminId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));
        User user = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        club.getAdmins().remove(user);
        clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    @Operation(summary = "Добавить участника в клуб")
    @Transactional
    public ClubDto addMemberToClub(UUID clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        if (!club.getMembers().contains(user)) {
            club.getMembers().add(user);
            clubRepository.save(club);
        }
        return clubMapper.toDto(club);
    }

    @Operation(summary = "Удалить участника из клуба")
    @Transactional
    public ClubDto removeMemberFromClub(UUID clubId, Long memberId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));
        User user = userRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        club.getMembers().remove(user);
        clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    @Operation(summary = "Получить всех участников клуба")
    public List<UserDto> getAllMembers(UUID clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));

        return club.getMembers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Получить всех админов клуба")
    public List<UserDto> getAllAdmins(UUID clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));

        return club.getAdmins().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addImageToClub(UUID clubId, UUID imageId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Клуб не найден"));
        Image img = imageService.getImageEntity(imageId);
        club.getImages().add(img);
        clubRepository.save(club);
    }

//    public UUID addClubImage(UUID clubId, MultipartFile file) throws IOException {
//        Club club = clubRepository.findById(clubId)
//                .orElseThrow(() -> new ResourceNotFoundException("Клуб не найден"));
//        UUID imageId = imageService.uploadImage(file);
//        club.getImages().add(imageService.getImageEntity(imageId));
//        // возможно, вы захотите сразу флешнуть entityManager, но save() тоже ок
//        clubRepository.save(club);
//        return imageId;
//    }

    public List<UUID> listClubImages(UUID clubId) {
        return clubRepository.findById(clubId)
                .orElseThrow(() -> new ResourceNotFoundException("Клуб не найден"))
                .getImages().stream()
                .map(Image::getId)
                .toList();
    }

//    public byte[] downloadClubImage(UUID clubId, UUID imageId) throws IOException, DataFormatException {
//        Club club = clubRepository.findById(clubId)
//                .orElseThrow(() -> new ResourceNotFoundException("Клуб не найден"));
//        boolean contains = club.getImages().stream()
//                .anyMatch(img -> img.getId().equals(imageId));
//        if (!contains) {
//            throw new ResourceNotFoundException("Фото не принадлежит этому клубу");
//        }
//        return imageService.downloadImage(imageId);
//    }
}
