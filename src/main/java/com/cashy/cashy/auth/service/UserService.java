package com.cashy.cashy.auth.service;

import com.cashy.cashy.auth.dto.UserLoginRequestDTO;
import com.cashy.cashy.auth.dto.UserSignupResponseDTO;
import com.cashy.cashy.auth.exception.EmailAlreadyExistsException;
import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.dto.UserProfileSignupDTO;
import com.cashy.cashy.auth.mapper.UserProfileMapper;
import com.cashy.cashy.auth.repository.UserProfileRepository;
import com.cashy.cashy.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserSignupResponseDTO registerUser(UserProfileSignupDTO userProfileSignupDTO) {
        if(userProfileRepository.existsByEmail(userProfileSignupDTO.getEmail())) {
            throw new EmailAlreadyExistsException(userProfileSignupDTO.getEmail());
        }
        userProfileSignupDTO.setPassword(passwordEncoder.encode(userProfileSignupDTO.getPassword()));
        UserProfile userProfile = UserProfileMapper.toEntity(userProfileSignupDTO);
        userProfileRepository.save(userProfile);
        return UserSignupResponseDTO
                .builder()
                .username(userProfile.getUsername())
                .email(userProfile.getEmail())
                .createdAt(userProfile.getCreatedAt().toString())
                .build();
    }

    public Optional<String> authenticateUser(UserLoginRequestDTO  userLoginRequestDTO) {
        return userProfileRepository.findByEmail(userLoginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(userLoginRequestDTO.getPassword(), u.getPassword()))
                .map(u -> jwtUtil.generateToken(u.getEmail(), String.valueOf(u.getRole())));
    }
}
