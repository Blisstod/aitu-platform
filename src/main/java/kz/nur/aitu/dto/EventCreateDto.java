package kz.nur.aitu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import kz.nur.aitu.enums.EventFormat;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCreateDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private EventFormat format;

    @NotBlank
    private String address;

    @NotNull
    @Schema(description = "Время когда начнется event", example = "2025-06-30 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;

    @NotNull
    @Schema(description = "Время когда закончится event", example = "2025-06-30 12:00:00")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    /** Списки айдишников пользователей (участников и админов) */
    private Set<@NotNull Long> participantIds;

    private Set<@NotNull Long> adminIds;
}
