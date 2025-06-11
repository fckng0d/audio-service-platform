package com.github.fckng0d.apigateway.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaimsResponseDto {
    private String userId;
    private List<String> roles;
    private Instant expiresAt;
}

