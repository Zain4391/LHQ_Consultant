package com.LHQ_Backend.LHQ_Backend.user.mapper;

import com.LHQ_Backend.LHQ_Backend.user.DTOs.Response.UserResponse;
import com.LHQ_Backend.LHQ_Backend.user.entity.User;

public final class UserMapper {

    private UserMapper() {}

    public static UserResponse toResponse(User user) {
        return UserResponse.builder().id(user.getId()).firstName(user.getFirstName())
                .lastName(user.getLastName()).email(user.getEmail()).age(user.getAge())
                .role(user.getRole()).profileImageUrl(user.getProfileImageUrl())
                .createdAt(user.getCreatedAt()).build();
    }
}
