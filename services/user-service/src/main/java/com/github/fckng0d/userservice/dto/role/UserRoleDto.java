package com.github.fckng0d.userservice.dto.role;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
