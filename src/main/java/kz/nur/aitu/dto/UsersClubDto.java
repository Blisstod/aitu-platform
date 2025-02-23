package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
@Schema(description = "DTO с информацией о пользователе и его клубах")
public class UsersClubDto {
    @Schema(description = "Уникальный идентификатор пользователя")
    private Long id;

    @Schema(description = "Имя пользователя")
    private String firstName;

    @Schema(description = "Фамилия пользователя")
    private String lastName;

    @Schema(description = "Email пользователя")
    private String email;

    @Schema(description = "Группа")
    private String department;

    @Schema(description = "Ключ безопасности")
    private String securityKey;

    @Schema(description = "Роль пользователя в системе")
    private String role;

    @Schema(description = "Список клубов пользователя")
    private List<ClubMembershipDto> clubs;
}
