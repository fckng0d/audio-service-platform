package com.github.fckng0d.dto.authenticationservice;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDto {
    @NotBlank
    private String accessToken;

    @NotBlank
    private String refreshToken;
}
