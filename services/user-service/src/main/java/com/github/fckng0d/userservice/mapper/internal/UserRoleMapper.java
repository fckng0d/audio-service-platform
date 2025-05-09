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
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserRoleMapper {

    @Mapping(source = "id", target = "role_id")
    UserRoleResponse toUserRoleResponse(UserRole userRole);

    CreateUserRoleRequestDto toCreateUserRoleDto(CreateRoleRequest request);

    @Mapping(source = "users", target = "user_ids", qualifiedByName = "mapUsersIds")
    UsersByRoleNameResponse toUsersByRoleNameResponse(UserRole userRole);

    @Named("mapUsersIds")
    default List<String> mapUsersIds(UserRole role) {
        return role.getUsers().stream()
                .map(User::getId)
                .map(UUID::toString)
                .collect(Collectors.toList());
    }

    @Mapping(source = "old_name", target = "oldName")
    @Mapping(source = "new_name", target = "newName")
    UpdateUserRoleRequestDto toUpdateUserRoleRequestDto(UpdateUserRoleRequest request);
}
