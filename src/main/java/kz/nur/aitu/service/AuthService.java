package kz.nur.aitu.service;

import kz.nur.aitu.dto.AuthResponse;
import kz.nur.aitu.dto.LoginRequest;
import kz.nur.aitu.dto.RegisterRequest;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.enums.Role;
import kz.nur.aitu.exception.UserAlreadyExistsException;
import kz.nur.aitu.mapper.UserMapper;
import kz.nur.aitu.repository.UserRepository;
import kz.nur.aitu.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MoodleService moodleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;

    @Transactional
    public UserDto registerUser(RegisterRequest request) {
        UserDto moodleUser = moodleService.getUserInfoFromMoodle(request.getSecurityKey());

        if (userRepository.existsById(moodleUser.getId())) {
            throw new UserAlreadyExistsException("Пользователь уже зарегистрирован.");
        }

        User newUser = userMapper.toEntity(moodleUser);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setSecurityKey(request.getSecurityKey());

        newUser.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        userRepository.save(newUser);
        return userMapper.toDto(newUser);
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);
    }
}
