package com.github.fckng0d.apigateway.mapper;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import com.github.fckng0d.grpc.trackservice.UploadFileRequest;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;
import com.github.fckng0d.grpc.trackservice.TrackResponse;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TrackMapper {
    default CreateTrackRequest toTrackRequest(CreateTrackDto requestDto) {
        return CreateTrackRequest.newBuilder()
                .setName(requestDto.getName())
                .addAllLanguages(requestDto.getLanguages().stream()
                        .map(Language::toString)
                        .toList())
                .addAllGenres(requestDto.getGenres().stream()
                        .map(MusicGenre::toString)
                        .toList())
                .addAllMusicianNicknames(requestDto.getMusicianNicknames())
                .setLyrics(requestDto.getLyrics())
                .setIsExplicit(requestDto.isExplicit())
                .setCoverImage(UploadFileRequest.newBuilder()
                        .setFileName(requestDto.getCoverImage().getFileName())
                        .setFileData(ByteString.copyFrom(requestDto.getCoverImage().getFileData()))
                        .build())
                .setTrackFile(UploadFileRequest.newBuilder()
                        .setFileName(requestDto.getTrackFile().getFileName())
                        .setFileData(ByteString.copyFrom(requestDto.getTrackFile().getFileData()))
                        .build())
                .build();
    }

    default TrackResponseDto toTrackResponseDto(TrackResponse response) {
        return TrackResponseDto.builder()
                .id(response.getId())
                .name(response.getName())
                .trackUrl(response.getTrackUrl())
                .durationSeconds((short) response.getDurationSeconds())
                .releaseDate(Instant.ofEpochSecond(
                        response.getReleaseDate().getSeconds(),
                        response.getReleaseDate().getNanos()
                ))
                .languages(response.getLanguagesList().stream()
                        .map(Language::valueOf)
                        .toList())
                .genres(response.getGenresList().stream()
                        .map(MusicGenre::valueOf)
                        .toList())
                .lyrics(response.getLyrics())
                .coverImageUrl(response.getCoverImageUrl())
                .albumId(UUID.fromString(response.getAlbumId()))
                .musicianNicknames(response.getMusicianNicknamesList())
                .isAvailable(response.getIsAvailable())
                .isExplicit(response.getIsExplicit())
                .auditionCount(response.getAuditionCount())
                .trackInFavoritesCount(response.getTrackInFavoritesCount())
                .build();
    }
}
