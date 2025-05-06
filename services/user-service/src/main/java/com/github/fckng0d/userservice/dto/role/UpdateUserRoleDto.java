package com.github.fckng0d.userservice.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRoleDto {
    @NotBlank
    private String oldName;

    @NotBlank
    private String newName;

    @NotBlank
    private String description;
}

