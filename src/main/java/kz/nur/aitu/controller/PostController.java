package kz.nur.aitu.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nur.aitu.dto.PostDto;
import kz.nur.aitu.entity.Post;
import kz.nur.aitu.mapper.PostMapper;
import kz.nur.aitu.repository.PostRepository;
import kz.nur.aitu.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rest/posts")
@Tag(name = "Посты", description = "API для постов")
//@RequiredArgsConstructor
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper;

    @Operation(summary = "Получить список всех постов",
            description = "Возвращает все посты в форме DTO")
    @GetMapping
    public List<PostDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @Operation(summary = "Получить один пост по UUID",
            description = "Возвращает один конкретный пост, если есть в базе")
    @GetMapping("/{id}")
    public PostDto getPostById(@PathVariable UUID id) {
        return postService.getPostById(id);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @Operation(summary = "Создать новый пост", description = "Только для пользователей с ролью ADMIN")
    public PostDto createPost(@RequestParam("image") MultipartFile image, @RequestParam("title") String title, @RequestParam("content") String content ) throws IOException {
        return postService.createPost(image, title, content);
    }

    @Operation(summary = "Удалить пост по UUID", description = "Только для пользователей с ролью ADMIN")
//    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
    }
}
