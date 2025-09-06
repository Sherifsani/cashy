package com.cashy.cashy.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDTO {
    @NotBlank(message = "email should not be blank")
    @Email(message = "enter a valid email address")
    private String email;

    @NotBlank(message = "password should not be blank")
    @Size(min = 6, message = "password must be at least 8 characters long")
    private String password;
}
