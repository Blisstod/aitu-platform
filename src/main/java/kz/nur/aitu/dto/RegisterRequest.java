package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import kz.nur.aitu.enums.Role;

@Data
@Schema(description = "Запрос на регистрацию пользователя")
public class RegisterRequest {
    @NotBlank
    @Schema(description = "Ключ безопасности", example = "mySecretKey123")
    private String securityKey;

    @NotBlank
    @Schema(description = "Пароль пользователя", example = "securePassword123")
    private String password;

    @Schema(description = "Роль пользователя", example = "USER")
    private Role role = Role.USER;
}
