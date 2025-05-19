package com.github.fckng0d.dto.authenticationservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthUserResponseDto {
    private UUID userId;
    private String username;
    private String email;
    private Set<String> roles;
    private String passwordHash;
}