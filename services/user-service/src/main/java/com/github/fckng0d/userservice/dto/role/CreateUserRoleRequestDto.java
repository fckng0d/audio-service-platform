package com.github.fckng0d.userservice.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRoleRequestDto {
    @NotBlank
    private String name;

    @NotBlank
    private String description;
}
