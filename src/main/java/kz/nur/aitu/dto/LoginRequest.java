package kz.nur.aitu.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
