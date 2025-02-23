package kz.nur.aitu.service;

import kz.nur.aitu.dto.*;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.enums.Role;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.mapper.ClubMapper;
import kz.nur.aitu.mapper.UserMapper;
import kz.nur.aitu.repository.ClubRepository;
import kz.nur.aitu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ClubRepository clubRepository;
    private final UserMapper userMapper;
    private final ClubMapper clubMapper;

    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        return userMapper.toDto(user);
    }

    @Transactional(readOnly = true)
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }

    public UsersClubDto getUserClubs(User user) {
        List<Club> clubs = clubRepository.findByAdminsContainingOrMembersContaining(user, user);

        List<ClubMembershipDto> clubDtos = clubs.stream()
                .map(club -> new ClubMembershipDto(
                        club.getId(),
                        club.getName(),
                        club.getDescription(),
                        club.getStatus().toString(),
                        club.getAdmins().contains(user) ? Role.ADMIN : Role.USER
                )).collect(Collectors.toList());

        return new UsersClubDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDepartment(),
                user.getSecurityKey(),
                user.getRole().name(),
                clubDtos
        );
    }
}
