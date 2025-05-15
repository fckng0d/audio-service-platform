package com.github.fckng0d.userservice.mapper.internal;

import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.grpc.userservice.UserResponse;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.user.CreateUserRequestDto;
import com.github.fckng0d.grpc.userservice.CreateUserRequest;
import com.google.protobuf.ProtocolStringList;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // CreateUserRequest -> CreateUserRequestDto
    CreateUserRequestDto toCreateUserRequestDto(CreateUserRequest createUserRequest);

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
//    @Mapping(target = "userId", source = "id", qualifiedByName = "mapUuidToString")
//    @Mapping(target = "rolesList", source = "roles", qualifiedByName = "mapUserRoles")
//    UserResponse toUserResponse(User user);


//    UserProfileDto toDto(UserProfile profile);
//
////    UserRoleDto toDto(UserRole role);
//
//    @Mapping(target = "user", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    UserProfile toEntity(UserProfileDto dto);

