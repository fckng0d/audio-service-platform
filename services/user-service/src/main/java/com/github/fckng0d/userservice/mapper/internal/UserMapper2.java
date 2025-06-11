package com.github.fckng0d.userservice.mapper.internal;

import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.grpc.userservice.UserResponse;
import com.github.fckng0d.userservice.domain.UserRole;
import org.mapstruct.Mapper;

import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper2 {

    // CreateUserRequest -> CreateUserRequestDto
//    default CreateUserRequestDto toCreateUserRequestDto(CreateUserRequest createUserRequest) {
//        return CreateUserRequestDto.builder()
//                .username(createUserRequest.getUsername())
//                .passwordHash(createUserRequest.getPasswordHash())
//                .email(createUserRequest.getEmail())
//                .build();
//    }

    // User -> UserResponse
    default UserResponse toUserResponse(User user) {
        return UserResponse.newBuilder()
                .setUserId(user.getId().toString())
                .setUsername(user.getUsername())
                .setEmail(user.getEmail())
                .addAllRoles(
                        user.getRoles().stream()
                                .map(UserRole::getName)
                                .collect(Collectors.toList())
                )
                .build();
    }
}

