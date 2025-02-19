package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nur.aitu.dto.AuthResponse;
import kz.nur.aitu.dto.LoginRequest;
import kz.nur.aitu.dto.RegisterRequest;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.service.AuthService;
import kz.nur.aitu.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/rest/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация", description = "API для входа и регистрации пользователей")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Регистрация пользователя", description = "Создает нового пользователя в системе")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Вход в систему", description = "Авторизует пользователя и возвращает JWT токен")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
