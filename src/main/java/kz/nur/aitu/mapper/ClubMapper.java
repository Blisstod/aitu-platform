package kz.nur.aitu.mapper;

import kz.nur.aitu.dto.ClubDto;
import kz.nur.aitu.dto.UserDto;
import kz.nur.aitu.entity.Club;
import kz.nur.aitu.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ClubMapper {

    @Mapping(target = "admins", source = "admins")
    @Mapping(target = "members", source = "members")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "imageIds",
            expression = "java(club.getImages().stream().map(img -> img.getId()).toList())")
    ClubDto toDto(Club club);

    @Mapping(target = "admins", ignore = true)
    @Mapping(target = "members", ignore = true)
    @Mapping(target = "status", source = "status")
    Club toEntity(ClubDto clubDto);

//    @Named("mapAdmins")
//    default List<UserDto> mapAdmins(List<User> admins) {
//        return admins != null ? admins.stream().map(user -> ).collect(Collectors.toList()) : null;
//    }
//
//    @Named("mapMembers")
//    default List<UserDto> mapMembers(List<User> members) {
//        return members != null ? members.stream().map(UserMapper::toDto).collect(Collectors.toList()) : null;
//    }
}
