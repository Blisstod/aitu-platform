package kz.nur.aitu.service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nur.aitu.dto.ClubDto;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.enums.ClubStatus;
import kz.nur.aitu.mapper.ClubMapper;
import kz.nur.aitu.mapper.UserMapper;
import kz.nur.aitu.repository.ClubRepository;
import kz.nur.aitu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Tag(name = "Club Service", description = "Сервис для управления клубами")
public class ClubService {

    @Autowired
    private ClubRepository clubRepository;
    @Autowired
    private ClubMapper clubMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userService;

    @Operation(summary = "Получить список всех клубов")
    public List<ClubDto> getAllClubs() {
        return clubRepository.findAll().stream()
                .map(clubMapper::toDto)
                .toList();
    }

    @Operation(summary = "Получить клуб по ID")
    public Optional<ClubDto> getClubById(UUID id) {
        return clubRepository.findById(id).map(clubMapper::toDto);
    }

    @Operation(summary = "Создать новый клуб")
    public ClubDto createClub(ClubDto clubDto) {
        Club club = clubMapper.toEntity(clubDto);

        if (clubDto.getStatus() == null || clubDto.getStatus().isBlank()) {
            club.setStatus(ClubStatus.ACTIVE);
        }

        club.setAdmins(Collections.singletonList(userService.getCurrentUser()));

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

    @Operation(summary = "Получить всех участников клуба, включая администраторов")
    public List<UserDto> getAllMembers(UUID clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(() -> new RuntimeException("Клуб не найден"));

        return club.getAdmins().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}
