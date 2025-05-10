package com.github.fckng0d.authenticationservice.mapper.grpc;

import com.github.fckng0d.dto.userservice.CreateUserRequestDto;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import com.github.fckng0d.grpc.userservice.CreateUserRequest;
import com.github.fckng0d.grpc.userservice.UserResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    CreateUserRequest toCreateUserRequest(CreateUserRequestDto dto);

    @Mapping(source = "roles", target = "roles")
    UserResponseDto toUserResponseDto(UserResponse response);
}
