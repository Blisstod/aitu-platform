package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.nur.aitu.enums.RequestStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplicationRequestCreateDto {
    @Schema(description = "ID формы, на которую отвечает студент", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID formId;

    @Schema(description = "JSON-ответ студента", example = "{\"answers\": [{\"questionId\": 1, \"answer\": \"I love programming.\"}]}")
    private String answerContent;

    @Schema(description = "Статус запроса", example = "IN_REVIEW")
    private RequestStatus status;
}
