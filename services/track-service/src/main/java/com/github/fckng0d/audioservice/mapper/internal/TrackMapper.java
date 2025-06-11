package com.github.fckng0d.audioservice.mapper.internal;

import com.github.fckng0d.audioservice.domain.Track;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;
import com.github.fckng0d.grpc.trackservice.TrackPreviewResponse;
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
                .musicianNicknames(dto.getMusicianNicknames())
                .lyrics(dto.getLyrics())
                .isAvailable(true)
                .isExplicit(dto.isExplicit())
                .auditionCount(0L)
                .trackInFavoritesCount(0L)
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
                .musicianNicknames(request.getMusicianNicknamesList())
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

    default TrackPreviewResponse toTrackPreviewResponse(TrackPreviewResponseDto dto) {
        return TrackPreviewResponse.newBuilder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setTrackUrl(dto.getTrackUrl())
                .setDurationSeconds(dto.getDurationSeconds())
                .setLyrics(dto.getLyrics())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .setAlbumId(dto.getAlbumId().toString())
                .addAllMusicianNicknames(dto.getMusicianNicknames())
                .setIsAvailable(dto.getIsAvailable())
                .setIsExplicit(dto.getIsExplicit())
                .setAuditionCount(dto.getAuditionCount())
                .setTrackInFavoritesCount(dto.getTrackInFavoritesCount())
                .build();
    }

    default TrackPreviewResponseDto toTrackPreviewResponseDto(Track track) {
        return TrackPreviewResponseDto.builder()
                .id(track.getId())
                .name(track.getName())
                .trackUrl(track.getTrackUrl())
                .durationSeconds(track.getDurationSeconds())
                .lyrics(track.getLyrics())
                .coverImageUrl(track.getCoverImageUrl())
                .albumId(track.getAlbumId())
                .musicianNicknames(track.getMusicianNicknames())
                .isAvailable(track.getIsAvailable())
                .isExplicit(track.getIsExplicit())
                .auditionCount(track.getAuditionCount())
                .trackInFavoritesCount(track.getTrackInFavoritesCount())
                .build();
    }

    default TrackResponseDto toTrackResponseDto(Track track) {
        return TrackResponseDto.builder()
                .id(track.getId())
                .name(track.getName())
                .trackUrl(track.getTrackUrl())
                .durationSeconds(track.getDurationSeconds())
                .releaseDate(track.getReleaseDate())
                .languages(track.getLanguages())
                .genres(track.getGenres())
                .lyrics(track.getLyrics())
                .coverImageUrl(track.getCoverImageUrl())
                .albumId(track.getAlbumId())
                .musicianNicknames(track.getMusicianNicknames())
                .isAvailable(track.getIsAvailable())
                .isExplicit(track.getIsExplicit())
                .auditionCount(track.getAuditionCount())
                .trackInFavoritesCount(track.getTrackInFavoritesCount())
                .build();
    }

    default TrackResponse toTrackResponse(TrackResponseDto dto) {
        return TrackResponse.newBuilder()
                .setId(dto.getId())
                .setName(dto.getName())
                .setTrackUrl(dto.getTrackUrl())
                .setDurationSeconds(dto.getDurationSeconds())
                .setReleaseDate(Timestamp.newBuilder()
                        .setSeconds(dto.getReleaseDate().getEpochSecond())
                        .setNanos(dto.getReleaseDate().getNano()))
                .addAllLanguages(dto.getLanguages().stream()
                        .map(Language::toString)
                        .toList())
                .addAllGenres(dto.getGenres().stream()
                        .map(MusicGenre::name)
                        .toList())
                .setLyrics(dto.getLyrics())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .setAlbumId(dto.getAlbumId().toString())
                .addAllMusicianNicknames(dto.getMusicianNicknames())
                .setIsAvailable(dto.getIsAvailable())
                .setIsExplicit(dto.getIsExplicit())
                .setAuditionCount(dto.getAuditionCount())
                .setTrackInFavoritesCount(dto.getTrackInFavoritesCount())
                .build();
    }
}
