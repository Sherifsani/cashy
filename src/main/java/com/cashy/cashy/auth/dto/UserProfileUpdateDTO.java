package com.cashy.cashy.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserProfileUpdateDTO {
    @NotBlank
    private String username;
    
    @Email
    @NotBlank
    private String email;
}
