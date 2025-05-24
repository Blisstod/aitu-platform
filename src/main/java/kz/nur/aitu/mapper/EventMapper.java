package kz.nur.aitu.mapper;

import kz.nur.aitu.dto.EventCreateDto;
import kz.nur.aitu.dto.EventDto;
import kz.nur.aitu.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface EventMapper {

    Event toEntity(EventCreateDto dto);

    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "admins", ignore = true)
    EventDto toDto(Event event);
}
