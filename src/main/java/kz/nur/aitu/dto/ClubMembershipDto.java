package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.nur.aitu.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.UUID;

@Data
@AllArgsConstructor
@Schema(description = "Клуб и роль пользователя в нем")
public class ClubMembershipDto {

    @Schema(description = "Уникальный идентификатор клуба")
    private UUID id;

    @Schema(description = "Название клуба")
    private String name;

    @Schema(description = "Описание клуба")
    private String description;

    @Schema(description = "Статус клуба")
    private String status;

    @Schema(description = "Роль пользователя в клубе")
    private Role role;
}
