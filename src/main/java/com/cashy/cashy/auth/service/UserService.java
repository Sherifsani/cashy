package com.cashy.cashy.auth.service;

import com.cashy.cashy.auth.dto.UserLoginRequestDTO;
import com.cashy.cashy.auth.dto.UserLoginResponseDTO;
import com.cashy.cashy.auth.dto.UserSignupResponseDTO;
import com.cashy.cashy.auth.exception.EmailAlreadyExistsException;
import com.cashy.cashy.auth.exception.InvalidCredentials;
import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.dto.UserProfileSignupDTO;
import com.cashy.cashy.auth.mapper.UserProfileMapper;
import com.cashy.cashy.auth.repository.UserProfileRepository;
import com.cashy.cashy.auth.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserProfileRepository userProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserSignupResponseDTO registerUser(UserProfileSignupDTO userProfileSignupDTO) {
        if (userProfileRepository.existsByEmail(userProfileSignupDTO.getEmail())) {
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

    public Optional<UserLoginResponseDTO> authenticateUser(UserLoginRequestDTO loginDTO) {
        return userProfileRepository.findByEmail(loginDTO.getEmail())
                .filter(user -> passwordEncoder.matches(loginDTO.getPassword(), user.getPassword()))
                .map(user -> new UserLoginResponseDTO(
                        jwtUtil.generateToken(user.getEmail(), user.getRole().name()),
                        user.getId(),
                        user.getEmail(),
                        user.getRole().name(),
                        user.getUsername()
                ))
                .or(() -> {
                    throw new InvalidCredentials("Invalid email or password.");
                });
    }

    public void saveUser(UserProfile userProfile) {
        userProfileRepository.save(userProfile);
    }

    public Optional<UserProfile> findUserById(UUID userId) {
        return userProfileRepository.findById(userId);
    }

    public boolean UserExistsById(UUID userId) {
        return userProfileRepository.existsById(userId);
    }

    public com.cashy.cashy.auth.dto.UserDetailDTO getUserProfile(UUID userId) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return com.cashy.cashy.auth.dto.UserDetailDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }

    public com.cashy.cashy.auth.dto.UserDetailDTO updateUserProfile(UUID userId, com.cashy.cashy.auth.dto.UserProfileUpdateDTO updateDTO) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (updateDTO.getEmail() != null) {
            user.setEmail(updateDTO.getEmail());
        }
        if (updateDTO.getUsername() != null) {
            user.setUsername(updateDTO.getUsername());
        }
        
        userProfileRepository.save(user);
        return getUserProfile(userId);
    }

    public void changePassword(UUID userId, com.cashy.cashy.auth.dto.PasswordChangeDTO passwordChangeDTO) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userProfileRepository.save(user);
    }

    public void deleteUser(UUID userId) {
        userProfileRepository.deleteById(userId);
    }

}
