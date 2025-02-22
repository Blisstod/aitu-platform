package kz.nur.aitu.entity;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "club_application_forms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Форма заявки на вступление в клуб")
public class ClubApplicationForm extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    @Schema(description = "Уникальный идентификатор формы", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    @Schema(description = "Идентификатор клуба, к которому относится форма", example = "c9a89b3c-2bd1-4c4f-889a-5c2ea6a7b2f7")
    private Club club;

    @Column(nullable = false)
    @Schema(description = "Дата окончания приема заявок", example = "2024-06-30 23:59:59")
    private LocalDateTime deadline;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    @Schema(description = "JSON-шаблон формы с вопросами", example = "{\"questions\": [{\"id\":1, \"question\": \"Why do you want to join?\"}]}")
    private String templateContent; // JSON с вопросами формы
}