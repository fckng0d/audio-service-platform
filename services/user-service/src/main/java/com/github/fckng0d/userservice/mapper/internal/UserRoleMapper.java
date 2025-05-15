package com.github.fckng0d.userservice.mapper.internal;

import com.github.fckng0d.grpc.userservice.UserRoleResponse;
import com.github.fckng0d.grpc.userservice.CreateRoleRequest;
import com.github.fckng0d.grpc.userservice.UsersByRoleNameResponse;
import com.github.fckng0d.grpc.userservice.UpdateUserRoleRequest;
import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.role.CreateUserRoleRequestDto;
import com.github.fckng0d.userservice.dto.role.UpdateUserRoleRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {
    UserRoleResponse toUserRoleResponse(UserRole userRole);

    CreateUserRoleRequestDto toCreateUserRoleDto(CreateRoleRequest request);

    default UsersByRoleNameResponse toUsersByRoleNameResponse(UserRole role) {
        return UsersByRoleNameResponse.newBuilder()
                .addAllUserIds(
                        role.getUsers().stream()
                                .map(User::getId)
                                .map(UUID::toString)
                                .collect(Collectors.toList())
                )
                .build();
    }

    UpdateUserRoleRequestDto toUpdateUserRoleRequestDto(UpdateUserRoleRequest request);
}
