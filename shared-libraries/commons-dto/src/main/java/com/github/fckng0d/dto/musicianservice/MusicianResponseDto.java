package com.github.fckng0d.dto.musicianservice;

import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MusicianResponseDto {
    private UUID musicianId;
    private UUID userId;
    private String nickname;
    private String bio;
    private Instant creationDate;
    private boolean isVerified;
    private boolean isBlocked;
    private String avatarImageUrl;
    private String headerImageUrl;
    List<AlbumPreviewResponseDto> albums;
    List<AlbumPreviewResponseDto> guestAlbums;
    List<UUID> subscriberIds;
}
