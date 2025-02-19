package kz.nur.aitu.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

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
    @Schema(description = "Уникальный идентификатор пользователя", example = "1")
    private Long id; // ID будет с внешнего сервиса

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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
