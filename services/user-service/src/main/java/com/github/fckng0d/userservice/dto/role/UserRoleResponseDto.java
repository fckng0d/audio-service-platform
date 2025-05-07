package com.github.fckng0d.userservice.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleResponseDto {
    @NotNull
    private Byte id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
