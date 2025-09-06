package com.cashy.cashy.auth.controller;

import com.cashy.cashy.auth.dto.UserProfileSignupDTO;
import com.cashy.cashy.auth.dto.UserSignupResponseDTO;
import com.cashy.cashy.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDTO> signUp(@RequestBody @Valid UserProfileSignupDTO userProfileSignupDTO) {
        UserSignupResponseDTO responseDTO = userService.registerUser(userProfileSignupDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
