package com.github.fckng0d.apigateway.mapper;

import com.github.fckng0d.dto.userservice.CreateUserRequestDto;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import com.github.fckng0d.grpc.userservice.UserResponse;
import com.github.fckng0d.grpc.userservice.GetUserByUsernameRequest;
import org.mapstruct.Mapper;

import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

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
