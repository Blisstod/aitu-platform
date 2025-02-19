package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO пользователя")
public class UserDto {
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id;

    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;

    @Schema(description = "Email пользователя", example = "221558@astanait.edu.kz")
    private String email;

    @Schema(description = "Группа", example = "SE-2204")
    private String department;

    @Schema(description = "Ключ безопасности", example = "mySecretKey123")
    private String securityKey;
}
