package kz.nur.aitu.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterRequest {
    @NotBlank
    private String securityKey;

    @NotBlank
    private String password;
}
