package com.cashy.cashy.auth.controller;

import com.cashy.cashy.auth.dto.*;
import com.cashy.cashy.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<UserSignupResponseDTO> signUp(
            @RequestBody
            @Valid
            UserProfileSignupDTO userProfileSignupDTO
    ) {
        UserSignupResponseDTO responseDTO = userService.registerUser(userProfileSignupDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> login(
            @RequestBody
            @Valid
            UserLoginRequestDTO userLoginRequestDTO
            ){
        Optional<String> tokenOptional = userService.authenticateUser(userLoginRequestDTO);
        if(tokenOptional.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = tokenOptional.get();
        return ResponseEntity.ok(new UserLoginResponseDTO(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailDTO> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
            ){
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        UserDetailDTO userDetailDTO = new UserDetailDTO(userDetails.getUsername());
        return ResponseEntity.ok(userDetailDTO);
    }
}
