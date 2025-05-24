package kz.nur.aitu.mapper;

import kz.nur.aitu.dto.EventCreateDto;
import kz.nur.aitu.dto.EventDto;
import kz.nur.aitu.entity.Event;
import kz.nur.aitu.entity.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EventMapper {

    Event toEntity(EventCreateDto dto);

    @Mapping(target = "participants", source = "participants")
    @Mapping(target = "admins",       source = "admins")
    @Mapping(target = "clubId", source = "club.id")
    @Mapping(target = "imageIds",
            expression = "java(event.getImages().stream().map(img -> img.getId()).toList())")
    EventDto toDto(Event event);
}
