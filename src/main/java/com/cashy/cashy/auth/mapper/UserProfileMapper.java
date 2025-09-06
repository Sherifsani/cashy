package com.cashy.cashy.auth.mapper;

import com.cashy.cashy.auth.model.UserProfile;
import com.cashy.cashy.auth.dto.UserProfileSignupDTO;

import java.time.LocalDateTime;

public  class UserProfileMapper {

    public static UserProfile toEntity(UserProfileSignupDTO signupDTO){
        return UserProfile.builder()
                .email(signupDTO.getEmail())
                .username(signupDTO.getUsername())
                .password(signupDTO.getPassword())
                .createdAt(LocalDateTime.now())
                .build();
    }

}
