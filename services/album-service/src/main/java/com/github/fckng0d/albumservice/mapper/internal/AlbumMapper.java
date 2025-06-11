package com.github.fckng0d.albumservice.mapper.internal;

import com.github.fckng0d.albumservice.domain.Album;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.grpc.albumservice.AlbumPreviewListResponse;
import com.github.fckng0d.grpc.trackservice.TrackPreviewResponse;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;

import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AlbumMapper {

    default Album.AlbumBuilder toAlbumBuilder(CreateAlbumDto albumDto) {
        return Album.builder()
                .name(albumDto.getName())
                .musicianNicknames(albumDto.getMusicianNicknames())
                .auditionCount(0L)
                .albumInFavoritesCount(0L);
    }

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
                .addAllLanguages(dto.getLanguages().stream()
                        .map(Language::toString)
                        .toList())
                .addAllGenres(dto.getGenres().stream()
                        .map(MusicGenre::name)
                        .toList())
                .setCoverImageUrl(dto.getCoverImageUrl())
                .addAllMusicianNicknames(dto.getMusicianNicknames())
                .addAllTrackGuestNicknames(dto.getTrackGuestNicknames())
                .addAllTracks(dto.getTracks().stream()
                        .map(track -> TrackPreviewResponse.newBuilder()
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


    default AlbumResponseDto toAlbumResponseDto(Album album, List<TrackPreviewResponseDto> trackPreviews) {
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
                .musicianNicknames(album.getMusicianNicknames())
                .trackGuestNicknames(album.getTrackGuestNicknames())
                .tracks(trackPreviews)
                .build();
    }

    default AlbumPreviewResponseDto toAlbumPreviewResponseDto(Album album) {
        return AlbumPreviewResponseDto.builder()
                .albumId(album.getId())
                .name(album.getName())
                .coverImageUrl(album.getCoverImageUrl())
                .releaseYear((short) album.getReleaseDate()
                        .atZone(ZoneId.systemDefault())
                        .getYear())
                .musicianNicknames(album.getMusicianNicknames())
                .isExplicit(album.getIsExplicit())
                .auditionCount(album.getAuditionCount())
                .build();
    }

    default List<AlbumPreviewResponseDto> toAlbumPreviewResponseDtoList(List<Album> albums) {
        return albums.stream()
                .map(this::toAlbumPreviewResponseDto)
                .toList();
    }

    default AlbumPreviewListResponse toAlbumPreviewListResponse(List<AlbumPreviewResponseDto> albumPreviews) {
        return AlbumPreviewListResponse.newBuilder()
                .addAllAlbums(albumPreviews.stream()
                        .map(albumPreview -> com.github.fckng0d.grpc.albumservice.AlbumPreviewResponse.newBuilder()
                                .setAlbumId(albumPreview.getAlbumId().toString())
                                .setName(albumPreview.getName())
                                .setCoverImageUrl(albumPreview.getCoverImageUrl())
                                .setReleaseYear(albumPreview.getReleaseYear())
                                .addAllMusicianNicknames(albumPreview.getMusicianNicknames())
                                .setIsExplicit(albumPreview.getIsExplicit())
                                .setAuditionCount(albumPreview.getAuditionCount())
                                .build())
                        .toList()
                )
                .build();
    }

}
