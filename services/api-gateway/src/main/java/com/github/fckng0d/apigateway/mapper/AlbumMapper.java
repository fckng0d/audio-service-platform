package com.github.fckng0d.apigateway.mapper;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AlbumMapper {
    default AlbumResponseDto toAlbumResponseDto(AlbumResponse response) {
        return AlbumResponseDto.builder()
                .albumId(UUID.fromString(response.getAlbumId()))
                .name(response.getName())
                .languages(response.getLanguagesList().stream()
                        .map(Language::valueOf)
                        .toList())
                .genres(response.getGenresList().stream()
                        .map(MusicGenre::valueOf)
                        .toList())
                .releaseDate(Instant.ofEpochSecond(
                        response.getReleaseDate().getSeconds(),
                        response.getReleaseDate().getNanos()
                ))
                .isAvailable(response.getIsAvailable())
                .isBlocked(response.getIsBlocked())
                .totalDurationSeconds(response.getTotalDurationSeconds())
                .auditionCount(response.getAuditionCount())
                .albumInFavoritesCount(response.getAlbumInFavoritesCount())
                .coverImageUrl(response.getCoverImageUrl())
                .musicianNicknames(response.getMusicianNicknamesList())
                .trackGuestNicknames(response.getTrackGuestNicknamesList())
                .tracks(response.getTracksList().stream()
                        .map(track -> TrackPreviewResponseDto.builder()
                                .id(track.getId())
                                .name(track.getName())
                                .trackUrl(track.getTrackUrl())
                                .durationSeconds((short) track.getDurationSeconds())
                                .coverImageUrl(track.getCoverImageUrl())
                                .albumId(UUID.fromString(track.getAlbumId()))
                                .musicianNicknames(track.getMusicianNicknamesList())
                                .isAvailable(track.getIsAvailable())
                                .isExplicit(track.getIsExplicit())
                                .auditionCount(track.getAuditionCount())
                                .trackInFavoritesCount(track.getTrackInFavoritesCount())
                                .build())
                        .toList())
                .build();
    }
}
