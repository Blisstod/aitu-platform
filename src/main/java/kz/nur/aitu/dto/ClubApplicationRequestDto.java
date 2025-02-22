package kz.nur.aitu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import kz.nur.aitu.enums.RequestStatus;
import kz.nur.aitu.enums.ResponseStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplicationRequestDto {

    @Schema(description = "Уникальный идентификатор заявки", example = "c7f39b3b-1eac-44b7-abc3-1a24a2b7a8a5")
    private UUID id;

    @Schema(description = "Кем был создан запрос", example = "221558@astanait.edu.kz")
    private String createdBy;

    @Schema(description = "Время когда был создан запрос", example = "2025-06-30 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "ID клуба, которому принадлежит форма", example = "a820eb86-ac48-486e-9ff6-a4a85e05b4fd")
    private UUID clubId;

    @Schema(description = "ID формы, на которую отвечает студент", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID formId;

    @Schema(description = "Статус запроса", example = "IN_REVIEW")
    private RequestStatus status;

    @Schema(description = "Ответ администрации", example = "ACCEPTED")
    private ResponseStatus response;

    @Schema(description = "Комментарий администрации", example = "You have been accepted based on your motivation.")
    private String responseMessage;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @Schema(description = "Дата ответа администратора", example = "2025-06-30 23:59:59")
    private LocalDateTime respondedDate;

    @Schema(description = "JSON-ответ студента", example = "{\"answers\": [{\"questionId\": 1, \"answer\": \"I love programming.\"}]}")
    private String answerContent;
}
