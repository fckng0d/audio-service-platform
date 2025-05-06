package com.github.fckng0d.userservice.dto.user;

import java.util.Set;
import java.util.UUID;

public class UserResponseDto {
    private UUID id;
    private String username;
    private String email;
//    private UserProfileResponseDTO profile;
    private Set<String> roles;
}
