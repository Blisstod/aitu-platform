package kz.nur.aitu.mapper;

import kz.nur.aitu.dto.PostDto;
import kz.nur.aitu.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "clubId", source = "club.id")
    @Mapping(target = "userId", source = "user.id")
    PostDto toDto(Post post);

    Post toEntity(PostDto postDto);
}
