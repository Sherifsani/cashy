package com.cashy.cashy.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class UserLoginResponseDTO {
    private String token;
    private UUID userId;
    private String email;
    private String role;
    private String username;
}
