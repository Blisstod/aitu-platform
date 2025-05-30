package kz.nur.aitu.service;

import kz.nur.aitu.dto.PostCreateDto;
import kz.nur.aitu.dto.PostDto;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.Image;
import kz.nur.aitu.entity.Post;
import kz.nur.aitu.entity.User;
import kz.nur.aitu.enums.Role;
import kz.nur.aitu.exception.ResourceNotFoundException;
import kz.nur.aitu.mapper.PostMapper;
import kz.nur.aitu.repository.ClubRepository;
import kz.nur.aitu.repository.PostRepository;
import kz.nur.aitu.repository.UserRepository;
import kz.nur.aitu.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    private final ClubRepository clubRepository;
    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ImageService imageService;
    @Autowired
    private UserService userService;
    @Autowired
    private SecurityUtils securityUtils;

    @Transactional
    public PostDto createPost(PostCreateDto dto) throws IOException {

        User user = securityUtils.getCurrentUser();

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setUser(user);

        if (dto.getClubId() != null) {
            Club club = clubRepository.findById(dto.getClubId())
                    .orElseThrow(() -> new ResourceNotFoundException("Клуб не найден"));
            if (!club.getAdmins().contains(user)) {
                throw new AccessDeniedException("Только админы клуба могут создавать его посты");
            }
            post.setClub(club);
        } else {
            // университетский пост
            if (user.getRole() != Role.ADMIN) {
                throw new AccessDeniedException("Только университетские админы могут создавать глобальные посты");
            }
            post.setClub(null);
        }

        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostDto> getAllPosts() {
        return postRepository.findAll()
            .stream()
            .map(postMapper::toDto)
            .collect(Collectors.toList());
    }

    public PostDto getPostById(UUID uuid) {
        Post post = postRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Пост не найден: " + uuid));
        return postMapper.toDto(post);
    }

    @Transactional
    public void deletePost(UUID uuid) {
        Post post = postRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("Пост не найден: " + uuid));
        postRepository.delete(post);
    }

    @Transactional
    public List<PostDto> getPostsByClub(UUID clubId) {
        if (!clubRepository.existsById(clubId)) {
            throw new ResourceNotFoundException("Клуб не найден");
        }
        return postRepository.findByClub_Id(clubId).stream()
                .map(postMapper::toDto)
                .toList();
    }
}
