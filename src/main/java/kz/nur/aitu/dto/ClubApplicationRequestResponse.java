package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.nur.aitu.enums.ResponseStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClubApplicationRequestResponse {

    @Schema(description = "ID запроса, на которую отвечает админ", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID requestId;

    @Schema(description = "Ответ администрации", example = "ACCEPTED")
    private ResponseStatus response;

    @Schema(description = "Комментарий администрации", example = "You have been accepted based on your motivation.")
    private String responseMessage;
}
