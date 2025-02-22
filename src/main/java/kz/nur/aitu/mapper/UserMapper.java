package kz.nur.aitu.mapper;

import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "securityKey", ignore = true)
    @Mapping(target = "role", ignore = true)
    User toEntity(UserDto userDto);
}
