package kz.nur.aitu.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kz.nur.aitu.entity.Image;
import kz.nur.aitu.entity.Post;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.repository.ImageRepository;
import kz.nur.aitu.repository.PostRepository;
import kz.nur.aitu.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ContextedRuntimeException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import java.util.zip.DataFormatException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

    private final ImageRepository imageRepository;
    private final PostRepository postRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public UUID uploadImage(MultipartFile imageFile) throws IOException {

        Image image = new Image();
        image.setName(imageFile.getOriginalFilename());
        image.setType(imageFile.getContentType());
        image.setImageData(ImageUtils.compressImage(imageFile.getBytes()));

        entityManager.persist(image);
        return image.getId();
    }

    public byte[] downloadImage(UUID id) {
        Optional<Image> dbImageOpt = imageRepository.findById(id);
        if (dbImageOpt.isEmpty()) {
            // можете бросить 404 или вернуть null
            throw new RuntimeException("Image not found: " + id);
        }
        Image dbImage = dbImageOpt.get();

        // Декомпрессируем, если сохраняли сжатое
        try {
            return ImageUtils.decompressImage(dbImage.getImageData());
        } catch (DataFormatException | IOException ex) {
            throw new ContextedRuntimeException("Error downloading an image", ex)
                    .addContextValue("Image ID", dbImage.getId())
                    .addContextValue("Image id", id);
        }
    }

    @Transactional(readOnly = true)
    public Image getImageEntity(UUID id) {
        return imageRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Изображение не найдено: " + id)
                );
    }
}
