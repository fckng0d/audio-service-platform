package com.github.fckng0d.userservice.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileDto {
    private UUID id;
    private String imageUrl;
    private UUID musicianProfileId;
    private Instant registrationDate;
}
