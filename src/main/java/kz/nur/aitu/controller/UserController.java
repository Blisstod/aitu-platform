package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.ServletRequest;
import kz.nur.aitu.dto.ClubDto;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.dto.UsersClubDto;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.mapper.UserMapper;
import kz.nur.aitu.service.UserService;
import kz.nur.aitu.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/users")
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SecurityUtils securityUtils;

    @GetMapping
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех зарегистрированных пользователей")
    public ResponseEntity<List<UserDto>> getAllUsers(ServletRequest servletRequest) {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает информацию о конкретном пользователе по его ID")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/by-email")
    @Operation(summary = "Получить пользователя по email", description = "Возвращает информацию о конкретном пользователе по его email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя в системе")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.createUser(userDto));
    }

    @GetMapping("/visitor")
    @Operation(summary = "Получить клубы текущего пользователя", description = "Возвращает список клубов, где пользователь является админом или участником")
    public ResponseEntity<UsersClubDto> getUserAndClubs() {
        User currentUser = securityUtils.getCurrentUser();
        return ResponseEntity.ok(userService.getUserClubs(currentUser));
    }
}
