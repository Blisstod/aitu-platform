package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Запрос на вход в систему")
public class LoginRequest {
    @Schema(description = "Email пользователя", example = "221558@astanait.edu.kz")
    private String email;

    @Schema(description = "Пароль пользователя", example = "securePassword123")
    private String password;
}
