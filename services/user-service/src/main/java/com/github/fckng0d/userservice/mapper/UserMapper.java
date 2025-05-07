package com.github.fckng0d.userservice.mapper;

import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.grpc.userservice.UserResponse;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.user.CreateUserRequestDto;
import com.github.fckng0d.grpc.userservice.CreateUserRequest;
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
    @Mapping(target = "passwordHash", source = "password_hash")
    CreateUserRequestDto toCreateUserRequestDto(CreateUserRequest createUserRequest);

    // User -> UserResponse
    @Mapping(source = "id", target = "user_id", qualifiedByName = "mapUuidToString")
    @Mapping(source = "roles", target = "roles", qualifiedByName = "mapUserRoles")
    UserResponse toUserResponse(User user);

    @Named("mapUuidToString")
    default String mapUuidToString(UUID id) {
        return id != null ? id.toString() : "";
    }

    @Named("mapUserRoles")
    default List<String> mapUserRoles(Set<UserRole> roles) {
        return roles.stream()
                .map(UserRole::getName)
                .collect(Collectors.toList());
    }

//    UserProfileDto toDto(UserProfile profile);
//
////    UserRoleDto toDto(UserRole role);
//
//    @Mapping(target = "user", ignore = true)
//    @Mapping(target = "id", ignore = true)
//    UserProfile toEntity(UserProfileDto dto);
}
