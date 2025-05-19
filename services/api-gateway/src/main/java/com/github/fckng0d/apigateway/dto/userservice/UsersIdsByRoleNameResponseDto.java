package com.github.fckng0d.apigateway.dto.userservice;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NotNull
public class UsersIdsByRoleNameResponseDto {
    private List<UUID> usersIds;
}
