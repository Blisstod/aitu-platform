package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;

@Data
public class PostDto {

    @Schema(description = "UUID поста (при создании можно не указывать)",
            example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
    private UUID id;

    @Schema(description = "Заголовок", example = "AITU News #1")
    private String title;

    @Schema(description = "Текст/контент поста", example = "Содержимое новости")
    private String description;

    @Schema(description = "URL/путь к картинке", example = "http://some-url/image.png")
    private UUID imageId;

    @Schema(description = "Количество лайков", example = "3")
    private int likeCount;

    private UUID clubId;  // null для университета

    @Schema(description = "ID пользователя (владельца поста)", example = "6661")
    private Long userId;
}
