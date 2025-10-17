package com.cashy.cashy.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDetailDTO {
    private UUID id;
    private String username;
    private String email;
}
