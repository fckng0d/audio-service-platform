package com.github.fckng0d.apigateway.mapper;

import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import com.github.fckng0d.grpc.albumservice.AlbumPreviewResponse;
import com.github.fckng0d.grpc.musicianservice.MusicianResponse;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MusicianMapper {

    default MusicianResponseDto toMusicianResponseDto(MusicianResponse response) {
        return MusicianResponseDto.builder()
                .musicianId(UUID.fromString(response.getMusicianId()))
                .userId(UUID.fromString(response.getUserId()))
                .nickname(response.getNickname())
                .bio(response.getBio())
                .isVerified(response.getIsVerified())
                .isBlocked(response.getIsBlocked())
                .avatarImageUrl(response.getAvatarImageUrl())
                .headerImageUrl(response.getHeaderImageUrl())
                .albums(this.toAlbumPreviewResponseDtoList(response.getAlbumsList()))
                .creationDate(Instant.ofEpochSecond(
                                response.getCreationDate().getSeconds(),
                                response.getCreationDate().getNanos()
                        )
                )
                .subscriberIds(response.getSubscriberIdsList().stream()
                        .map(UUID::fromString)
                        .toList())
                .build();
    }

    default List<AlbumPreviewResponseDto> toAlbumPreviewResponseDtoList(List<AlbumPreviewResponse> albumPreviewResponseList) {
        return albumPreviewResponseList.stream()
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