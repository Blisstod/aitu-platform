package kz.nur.aitu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "images")
@Schema(description = "Сущность для хранения загруженных изображений в базе")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    @Schema(description = "Первичный ключ изображения", example = "1")
    private UUID id;

    @Column(name = "name")
    @Schema(description = "Имя файла (оригинальное)", example = "my_picture.png")
    private String name;

    @Column(name = "type")
    @Schema(description = "MIME-тип (например, image/png)", example = "image/png")
    private String type;

    @Column(name = "image_data")
    @Schema(description = "Сжатые (или просто бинарные) данные изображения")
    private byte[] imageData;
}
