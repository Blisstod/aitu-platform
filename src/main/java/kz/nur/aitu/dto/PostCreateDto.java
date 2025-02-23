package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
public class PostCreateDto {
    @Schema(description = "Image ID", example = "f42ebf23-ccf1-453d-ac0a-ce4ff86868c5")
    private UUID imageId;

    @Schema(description = "Заголовок", example = "AITU News #1")
    private String title;

    @Schema(description = "Текст/контент поста", example = "Содержимое новости")
    private String description;
}
