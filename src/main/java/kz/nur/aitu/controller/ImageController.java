package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import kz.nur.aitu.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/rest/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @Operation(summary = "Загрузить изображение",
            description = "Принимает multipart/form-data, сохраняет в БД")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UUID> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
        UUID result = imageService.uploadImage(file);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    @Operation(summary = "Загрузить изображение из БД по имени",
            description = "Возвращает бинарные данные изображения (считаем, что это PNG).")
    @GetMapping(value = "/{id}", produces = IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> downloadImage(@PathVariable UUID id) {
        byte[] imageData = imageService.downloadImage(id);
        return ResponseEntity
                .ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(imageData);
    }
}
