package kz.nur.aitu.dto;

import lombok.*;
import kz.nur.aitu.enums.EventFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {

    private UUID id;

    private String name;
    private String description;
    private EventFormat format;
    private String address;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private UUID clubId; // null для университетских
    /** Маппятся через UserDto */
    private Set<UserDto> participants;
    private Set<UserDto> admins;
    private List<UUID> imageIds;
}
