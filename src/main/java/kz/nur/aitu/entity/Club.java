package kz.nur.aitu.entity;

import jakarta.persistence.*;
import kz.nur.aitu.enums.ClubStatus;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "clubs")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Club extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToMany
    @JoinTable(
            name = "club_admins",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "admin_id")
    )
    private List<User> admins;

    @ManyToMany
    @JoinTable(
            name = "club_members",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "member_id")
    )
    private List<User> members;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClubStatus status = ClubStatus.ACTIVE;

    @ManyToMany
    @JoinTable(
            name = "club_images",
            joinColumns = @JoinColumn(name = "club_id"),
            inverseJoinColumns = @JoinColumn(name = "image_id")
    )
    @Builder.Default
    private List<Image> images = new ArrayList<>();
}
