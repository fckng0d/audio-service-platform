package com.github.fckng0d.dto.albumservice;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumResponseDto {
    private UUID albumId;
    private String name;
    private List<Language> languages;
    private List<MusicGenre> genres;
    private Instant releaseDate;
    private Boolean isAvailable;
    private Boolean isBlocked;
    private Integer totalDurationSeconds;
    private Long auditionCount;
    private Integer albumInFavoritesCount;
    private String coverImageUrl;
    private List<Long> trackIds;
    private List<UUID> musicianIds;
    private List<UUID> trackGuestIds;
}
