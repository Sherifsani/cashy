package com.cashy.cashy.auth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserSignupResponseDTO {
    private String email;
    private String username;
    private String createdAt;
}
