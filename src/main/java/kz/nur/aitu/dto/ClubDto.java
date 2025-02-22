package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO для клуба")
public class ClubDto {

    @Schema(description = "Уникальный идентификатор клуба", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Название клуба", example = "AITU Robotics Club")
    private String name;

    @Schema(description = "Описание клуба", example = "Клуб для любителей робототехники")
    private String description;

    @Schema(description = "Список администраторов клуба")
    private List<UserDto> admins;

    @Schema(description = "Список участников клуба")
    private List<UserDto> members;

    @Schema(description = "Статус клуба", example = "ACTIVE")
    private String status;
}
