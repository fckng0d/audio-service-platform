package com.github.fckng0d.apigateway.mapper.userservice;

import com.github.fckng0d.dto.userservice.UserResponseDto;
import com.github.fckng0d.grpc.userservice.UserResponse;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserResponse -> UserResponseDto
    default UserResponseDto toUserResponseDto(UserResponse response) {
        return UserResponseDto.builder()
                .userId(UUID.fromString(response.getUserId()))
                .username(response.getUsername())
                .email(response.getEmail())
                .roles(new HashSet<>(response.getRolesList()))
                .build();
    }
}
