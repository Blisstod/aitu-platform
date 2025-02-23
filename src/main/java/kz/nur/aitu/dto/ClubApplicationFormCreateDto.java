package kz.nur.aitu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplicationFormCreateDto {
    @Schema(description = "ID клуба, к которому относится форма", example = "c9a89b3c-2bd1-4c4f-889a-5c2ea6a7b2f7")
    private UUID clubId;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата окончания приема заявок", example = "2025-06-30 23:59:59")
    private LocalDateTime deadline;

    @Schema(description = "JSON-шаблон формы с вопросами", example = "{\"questions\": [{\"id\":1, \"question\": \"Why do you want to join?\"}]}")
    private String templateContent;
}
