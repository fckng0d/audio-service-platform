package com.github.fckng0d.dto.albumservice;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
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
    private Boolean isExplicit;
    private Boolean isAvailable;
    private Boolean isBlocked;
    private Integer totalDurationSeconds;
    private Long auditionCount;
    private Long albumInFavoritesCount;
    private String coverImageUrl;
    @Builder.Default
    private List<TrackPreviewResponseDto> tracks = new ArrayList<>();
    private List<String> musicianNicknames;
    @Builder.Default
    private List<String> trackGuestNicknames = new ArrayList<>();
}
