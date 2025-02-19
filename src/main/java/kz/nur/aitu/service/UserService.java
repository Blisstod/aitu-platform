package kz.nur.aitu.service;

import kz.nur.aitu.dto.AuthResponse;
import kz.nur.aitu.dto.LoginRequest;
import kz.nur.aitu.dto.RegisterRequest;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.exception.UserAlreadyExistsException;
import kz.nur.aitu.mapper.UserMapper;
import kz.nur.aitu.repository.UserRepository;
import kz.nur.aitu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

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

    @Transactional
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        user = userRepository.save(user);
        return userMapper.toDto(user);
    }
}
