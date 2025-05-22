package com.github.fckng0d.musicianservice.mapper.internal;

import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.musicianservice.domain.Musician;
import com.google.protobuf.ByteString;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.albumservice.UploadFileRequest;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    default CreateAlbumDto toCreateAlbumDto(CreateAlbumRequest request) {
        return CreateAlbumDto.builder()
                .name(request.getName())
                .tracks(request.getTracksList().stream()
                        .map(trackRequest -> CreateTrackDto.builder()
                                .name(trackRequest.getName())
                                .languages(trackRequest.getLanguagesList().stream()
                                        .map(Language::valueOf)
                                        .toList())
                                .genres(trackRequest.getGenresList().stream()
                                        .map(MusicGenre::valueOf)
                                        .toList())
                                .lyrics(trackRequest.getLyrics())
                                .musicianIds(trackRequest.getMusicianIdsList().stream()
                                        .map(UUID::fromString)
                                        .toList())
                                .coverImage(UploadFileDto.builder()
                                        .fileName(trackRequest.getCoverImage().getFileName())
                                        .fileData(trackRequest.getCoverImage().getFileData().toByteArray())
                                        .build())
                                .trackFile(UploadFileDto.builder()
                                        .fileName(trackRequest.getTrackFile().getFileName())
                                        .fileData(trackRequest.getTrackFile().getFileData().toByteArray())
                                        .build())
                                .build())
                        .toList())
                .build();
    }

    default AlbumResponse toAlbumResponse(AlbumResponseDto dto) {
        return AlbumResponse.newBuilder()
                .setAlbumId(dto.getAlbumId().toString())
                .setName(dto.getName())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .addAllMusicianIds(dto.getMusicianIds().stream()
                        .map(UUID::toString)
                        .toList())
                .addAllTrackGuestIds(dto.getTrackGuestIds().stream()
                        .map(UUID::toString)
                        .toList())
                .addAllTrackIds(dto.getTrackIds())
                .build();
    }

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
                .musicianIds(response.getMusicianIdsList().stream()
                        .map(UUID::fromString)
                        .toList())
                .trackGuestIds(response.getTrackGuestIdsList().stream()
                        .map(UUID::fromString)
                        .toList())
                .trackIds(response.getTrackIdsList())
                .build();
    }

    default CreateAlbumRequest toCreateAlbumRequest(CreateAlbumDto dto) {
        return CreateAlbumRequest.newBuilder()
                .setName(dto.getName())
                .addAllMusicianIds(dto.getMusicianIds().stream()
                        .map(UUID::toString)
                        .toList())
                .setCoverImage(UploadFileRequest.newBuilder()
                        .setFileName(dto.getCoverImage().getFileName())
                        .setFileData(ByteString.copyFrom(dto.getCoverImage().getFileData()))
                        .build()
                )
                .addAllTracks(dto.getTracks().stream()
                        .map(trackDto -> CreateTrackRequest.newBuilder()
                                .setAlbumId(trackDto.getAlbumId().toString())
                                .setName(trackDto.getName())
                                .addAllLanguages(trackDto.getLanguages().stream()
                                        .map(Language::toString)
                                        .toList())
                                .addAllGenres(trackDto.getGenres().stream()
                                        .map(MusicGenre::toString)
                                        .toList())
                                .setLyrics(trackDto.getLyrics())
                                .setIsExplicit(trackDto.isExplicit())
                                .addAllMusicianIds(trackDto.getMusicianIds().stream()
                                        .map(UUID::toString)
                                        .toList())
                                .setCoverImage(com.github.fckng0d.grpc.trackservice.UploadFileRequest.newBuilder()
                                        .setFileName(trackDto.getCoverImage().getFileName())
                                        .setFileData(ByteString.copyFrom(trackDto.getCoverImage().getFileData()))
                                        .build())
                                .setTrackFile(com.github.fckng0d.grpc.trackservice.UploadFileRequest.newBuilder()
                                        .setFileName(trackDto.getTrackFile().getFileName())
                                        .setFileData(ByteString.copyFrom(trackDto.getTrackFile().getFileData()))
                                        .build())
                                .build())
                        .toList())
                .build();
    }
}
