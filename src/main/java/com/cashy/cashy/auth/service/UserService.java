package com.cashy.cashy.auth.service;

import com.cashy.cashy.auth.dto.UserSignupResponseDTO;
import com.cashy.cashy.auth.exception.EmailAlreadyExistsException;
import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.dto.UserProfileSignupDTO;
import com.cashy.cashy.auth.mapper.UserProfileMapper;
import com.cashy.cashy.auth.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;

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
}
