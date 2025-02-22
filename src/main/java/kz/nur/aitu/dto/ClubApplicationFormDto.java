package kz.nur.aitu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplicationFormDto {

    @Schema(description = "Уникальный идентификатор формы", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @Schema(description = "Человек который создал форму", example = "221558@astanait.edu.kz")
    private String createdBy;

    @Schema(description = "День когда был создана форма", example = "2025-06-30 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "ID клуба, к которому относится форма", example = "c9a89b3c-2bd1-4c4f-889a-5c2ea6a7b2f7")
    private UUID clubId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата окончания приема заявок", example = "2025-06-30 23:59:59")
    private LocalDateTime deadline;

    @Schema(description = "JSON-шаблон формы с вопросами", example = "{\"questions\": [{\"id\":1, \"question\": \"Why do you want to join?\"}]}")
    private String templateContent;
}
