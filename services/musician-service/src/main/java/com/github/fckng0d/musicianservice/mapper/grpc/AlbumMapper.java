package com.github.fckng0d.musicianservice.mapper.grpc;

import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.albumservice.UploadFileRequest;
import com.github.fckng0d.grpc.albumservice.AlbumPreviewListResponse;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;

import java.time.Instant;
import java.util.List;
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
                                .musicianNicknames(trackRequest.getMusicianNicknamesList())
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
                .addAllTrackGuestNicknames(dto.getMusicianNicknames())
                .addAllTrackGuestNicknames(dto.getTrackGuestNicknames())
                .addAllTracks(dto.getTracks().stream()
                        .map(track -> com.github.fckng0d.grpc.trackservice.TrackPreviewResponse.newBuilder()
                                .setId(track.getId())
                                .setName(track.getName())
                                .setTrackUrl(track.getTrackUrl())
                                .setDurationSeconds(track.getDurationSeconds())
                                .setLyrics(track.getLyrics())
                                .setCoverImageUrl(track.getCoverImageUrl())
                                .setAlbumId(track.getAlbumId().toString())
                                .addAllMusicianNicknames(track.getMusicianNicknames())
                                .setIsAvailable(track.getIsAvailable())
                                .setIsExplicit(track.getIsExplicit())
                                .setTrackInFavoritesCount(track.getTrackInFavoritesCount())
                                .build())
                        .toList())
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

    default CreateAlbumRequest toCreateAlbumRequest(CreateAlbumDto dto) {
        return CreateAlbumRequest.newBuilder()
                .setName(dto.getName())
                .addAllMusicianNicknames(dto.getMusicianNicknames())
                .setCoverImage(UploadFileRequest.newBuilder()
                        .setFileName(dto.getCoverImage().getFileName())
                        .setFileData(ByteString.copyFrom(dto.getCoverImage().getFileData()))
                        .build()
                )
                .addAllTracks(dto.getTracks().stream()
                        .map(trackDto -> CreateTrackRequest.newBuilder()
                                .setName(trackDto.getName())
                                .addAllLanguages(trackDto.getLanguages().stream()
                                        .map(Language::toString)
                                        .toList())
                                .addAllGenres(trackDto.getGenres().stream()
                                        .map(MusicGenre::toString)
                                        .toList())
                                .setLyrics(trackDto.getLyrics())
                                .setIsExplicit(trackDto.isExplicit())
                                .addAllMusicianNicknames(trackDto.getMusicianNicknames())
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

    default List<AlbumPreviewResponseDto> toAlbumPreviewResponseDtoList(AlbumPreviewListResponse response) {
        return response.getAlbumsList().stream()
                .map(albumPreview -> AlbumPreviewResponseDto.builder()
                        .albumId(UUID.fromString(albumPreview.getAlbumId()))
                        .name(albumPreview.getName())
                        .coverImageUrl(albumPreview.getCoverImageUrl())
                        .releaseYear((short) albumPreview.getReleaseYear())
                        .musicianNicknames(albumPreview.getMusicianNicknamesList())
                        .isExplicit(albumPreview.getIsExplicit())
                        .build())
                .toList();
    }
}
