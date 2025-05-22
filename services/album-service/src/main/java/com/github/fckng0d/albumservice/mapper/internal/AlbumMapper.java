package com.github.fckng0d.albumservice.mapper.internal;

import com.github.fckng0d.albumservice.domain.Album;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;

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
                .addAllLanguages(dto.getLanguages().stream()
                        .map(Language::toString)
                        .toList())
                .addAllGenres(dto.getGenres().stream()
                        .map(MusicGenre::name)
                        .toList())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .addAllMusicianIds(dto.getMusicianIds().stream()
                        .map(UUID::toString)
                        .toList())
                .addAllTrackGuestIds(dto.getTrackGuestIds().stream()
                        .map(UUID::toString)
                        .toList())
                .addAllTrackIds(dto.getTrackIds())
                .setIsAvailable(dto.getIsAvailable())
                .setIsBlocked(dto.getIsBlocked())
                .setTotalDurationSeconds(dto.getTotalDurationSeconds())
                .setAuditionCount(dto.getAuditionCount())
                .setAlbumInFavoritesCount(dto.getAlbumInFavoritesCount())
                .setReleaseDate(Timestamp.newBuilder()
                        .setSeconds(dto.getReleaseDate().getEpochSecond())
                        .setNanos(dto.getReleaseDate().getNano())
                        .build())
                .build();
    }


    default AlbumResponseDto toAlbumResponseDto(Album album) {
        return AlbumResponseDto.builder()
                .albumId(album.getId())
                .name(album.getName())
                .languages(album.getLanguages())
                .genres(album.getGenres())
                .releaseDate(album.getReleaseDate())
                .isAvailable(album.getIsAvailable())
                .isBlocked(album.getIsBlocked())
                .totalDurationSeconds(album.getTotalDurationSeconds())
                .auditionCount(album.getAuditionCount())
                .albumInFavoritesCount(album.getAlbumInFavoritesCount())
                .coverImageUrl(album.getCoverImageUrl())
                .trackIds(album.getTrackIds())
                .musicianIds(album.getMusicianIds())
                .trackGuestIds(album.getTrackGuestIds())
                .build();
    }

}
