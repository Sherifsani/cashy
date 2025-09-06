package com.cashy.cashy.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class UserProfileSignupDTO {
    @Size(max = 50)
    @NotBlank(message = "username should not be blank")
    private String username;

    @Email(message = "please enter a valid email address")
    @NotBlank(message = "email is required")
    private String email;

    @NotBlank(message = "password should not be blank")
    @Size(min = 6, message = "password must be at least 8 characters long")
    private String password;

}
