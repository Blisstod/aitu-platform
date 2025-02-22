package kz.nur.aitu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import kz.nur.aitu.enums.Role;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сущность пользователя")
public class User extends Auditable implements UserDetails {

    @Id
    @Schema(description = "Уникальный идентификатор пользователя", example = "6661")
    private Long id;

    @Column
    @Schema(description = "Имя пользователя", example = "John")
    private String firstName;

    @Column
    @Schema(description = "Фамилия пользователя", example = "Doe")
    private String lastName;

    @Column(unique = true)
    @Schema(description = "Email пользователя", example = "221558@astanait.edu.kz")
    private String email;

    @Column
    @Schema(description = "Группа", example = "SE-2204")
    private String department;

    @Column
    @Schema(description = "Пароль пользователя", example = "hashedPassword123")
    private String password;

    @Column(nullable = false)
    @Schema(description = "Ключ безопасности", example = "mySecretKey123")
    private String securityKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Schema(description = "Роль пользователя", example = "USER")
    private Role role;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Post> posts;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
