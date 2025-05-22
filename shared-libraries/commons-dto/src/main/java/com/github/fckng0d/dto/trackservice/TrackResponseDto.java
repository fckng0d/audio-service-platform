package com.github.fckng0d.dto.trackservice;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
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
public class TrackResponseDto {
    private Long id;
    private String name;
    private String trackUrl;
    private Short durationSeconds;
    private Instant releaseDate;
    private List<Language> languages;
    private List<MusicGenre> genres;
    private String lyrics;
    private String coverImageUrl;
    private UUID albumId;
    private List<UUID> musicianIds;
    private Boolean isAvailable;
    private Boolean isExplicit;
    private Long auditionCount;
    private Integer trackInFavoritesCount;
}
