package com.github.fckng0d.audioservice.mapper.internal;

import com.github.fckng0d.audioservice.domain.Track;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;
import com.github.fckng0d.grpc.trackservice.TrackResponse;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TrackMapper {

    default Track toTrack (CreateTrackDto dto) {
        return Track.builder()
                .albumId(dto.getAlbumId())
                .name(dto.getName())
                .languages(dto.getLanguages())
                .genres(dto.getGenres())
                .musicianIds(dto.getMusicianIds())
                .lyrics(dto.getLyrics())
                .isAvailable(true)
                .isExplicit(dto.isExplicit())
                .auditionCount(0L)
                .trackInFavoritesCount(0)
                .build();
    }

    default CreateTrackDto toCreateTrackDto(CreateTrackRequest request) {
        return CreateTrackDto.builder()
                .albumId(UUID.fromString(request.getAlbumId()))
                .name(request.getName())
                .languages(request.getLanguagesList().stream()
                        .map(Language::valueOf)
                        .toList())
                .genres(request.getGenresList().stream()
                        .map(MusicGenre::valueOf)
                        .toList())
                .musicianIds(request.getMusicianIdsList().stream()
                        .map(UUID::fromString)
                        .toList())
                .lyrics(request.getLyrics())
                .isExplicit(request.getIsExplicit())
                .coverImage(UploadFileDto.builder()
                        .fileName(request.getCoverImage().getFileName())
                        .fileData(request.getCoverImage().getFileData().toByteArray())
                        .build())
                .trackFile(UploadFileDto.builder()
                        .fileName(request.getTrackFile().getFileName())
                        .fileData(request.getTrackFile().getFileData().toByteArray())
                        .build())
                .build();
    }

    default TrackResponse toTrackResponse(TrackResponseDto dto) {
        return TrackResponse.newBuilder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setAlbumId(dto.getAlbumId().toString())
                .setTrackUrl(dto.getTrackUrl())
                .setDurationSeconds(dto.getDurationSeconds())
                .setReleaseDate(Timestamp.newBuilder()
                                .setSeconds(dto.getReleaseDate().getEpochSecond())
                                .setNanos(dto.getReleaseDate().getNano())
                                .build()
                )
                .addAllGenres(dto.getGenres().stream()
                        .map(MusicGenre::toString)
                        .toList())
                .addAllLanguages(dto.getLanguages().stream()
                        .map(Language::toString)
                        .toList())
                .setLyrics(dto.getLyrics())
                .setIsAvailable(dto.getIsAvailable())
                .setIsExplicit(dto.getIsExplicit())
                .setAuditionCount(dto.getAuditionCount())
                .setTrackInFavoritesCount(dto.getTrackInFavoritesCount())
                .build();
    }

    default TrackResponseDto toTrackResponseDto(Track track) {
        return TrackResponseDto.builder()

                .build();
    }
}
