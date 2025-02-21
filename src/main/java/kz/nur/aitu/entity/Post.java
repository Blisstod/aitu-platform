package kz.nur.aitu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Сущность постов")
public class Post extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    @Schema(description = "Уникальный идентификатор пользователя", example = "89b4de53-e59f-461f-a2b1-faf96239dbed")
    private UUID id;

    @Column(name = "title")
    @Schema(description = "Заголовок поста", example = "AITU News #1")
    private String title;

    @Column(name = "description")
    @Schema(description = "Текст/контент поста", example = "Содержимое новости или описания")
    private String description;

    @Column(name = "like_count")
    @Schema(description = "Количество лайков", example = "0")
    private int likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @Schema(description = "Пользователь-владелец поста")
    private User user;

    @Column(name = "image_id")
    private UUID imageId;
}
