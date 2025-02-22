package kz.nur.aitu.mapper;

import kz.nur.aitu.dto.ClubApplicationFormDto;
import kz.nur.aitu.dto.ClubApplicationRequestDto;
import kz.nur.aitu.entity.ClubApplicationForm;
import kz.nur.aitu.entity.ClubApplicationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClubApplicationMapper {

    @Mapping(target = "clubId", source = "club.id")
    ClubApplicationFormDto toDto(ClubApplicationForm form);
    @Mapping(target = "club", ignore = true)
    ClubApplicationForm toEntity(ClubApplicationFormDto formDto);

    @Mapping(target = "clubId", source = "clubApplicationForm.club.id")
    @Mapping(target = "formId", source = "clubApplicationForm.id")
    ClubApplicationRequestDto toDto(ClubApplicationRequest request);
    @Mapping(target = "clubApplicationForm", ignore = true)
    ClubApplicationRequest toEntity(ClubApplicationRequestDto requestDto);
}
