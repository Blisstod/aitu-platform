package kz.nur.aitu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import kz.nur.aitu.enums.RequestStatus;
import kz.nur.aitu.enums.ResponseStatus;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "club_application_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ студента на заявку в клуб")
public class ClubApplicationRequest extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    @Schema(description = "Уникальный идентификатор запроса", example = "c7f39b3b-1eac-44b7-abc3-1a24a2b7a8a5")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "form_id", nullable = false)
    @Schema(description = "Идентификатор формы, на которую студент отвечает", example = "550e8400-e29b-41d4-a716-446655440000")
    private ClubApplicationForm clubApplicationForm;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Статус запроса", example = "IN_REVIEW")
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Ответ администрации (принято/отклонено)", example = "ACCEPTED")
    private ResponseStatus response;

    @Column(length = 500)
    @Schema(description = "Комментарий администрации", example = "You have been accepted based on your motivation.")
    private String responseMessage;

    @Schema(description = "Дата ответа администратора", example = "2024-06-30T23:59:59")
    private LocalDateTime respondedDate;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    @Schema(description = "JSON-ответ студента", example = "{\"answers\": [{\"questionId\": 1, \"answer\": \"I love programming.\"}]}")
    private String answerContent;
}
