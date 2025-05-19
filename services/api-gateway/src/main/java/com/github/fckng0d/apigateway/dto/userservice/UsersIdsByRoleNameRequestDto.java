package com.github.fckng0d.apigateway.dto.userservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersIdsByRoleNameRequestDto {
    private String roleName;
}
