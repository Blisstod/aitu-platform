package kz.nur.aitu.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
@Getter
@Setter
public class PostCreateDto {
    @Schema(description = "Заголовок", example = "AITU News #1")
    private String title;

    @Schema(description = "Текст/контент поста", example = "Содержимое новости")
    private String description;
}
