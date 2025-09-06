package com.cashy.cashy.auth.controller;

import com.cashy.cashy.auth.dto.UserLoginRequestDTO;
import com.cashy.cashy.auth.dto.UserLoginResponseDTO;
import com.cashy.cashy.auth.dto.UserProfileSignupDTO;
import com.cashy.cashy.auth.dto.UserSignupResponseDTO;
import com.cashy.cashy.auth.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
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
}
