package com.github.fckng0d.musicianservice.mapper.internal;

import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import com.github.fckng0d.musicianservice.domain.Musician;
import com.google.protobuf.Timestamp;
import org.mapstruct.Mapper;
import com.github.fckng0d.grpc.musicianservice.MusicianResponse;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MusicianMapper {

    default MusicianResponseDto.MusicianResponseDtoBuilder toMusicianResponseDtoBuilder(Musician musician) {
        return MusicianResponseDto.builder()
                .musicianId(musician.getId())
                .userId(musician.getUserId())
                .nickname(musician.getNickname())
                .bio(musician.getBio())
                .isVerified(musician.getIsVerified())
                .isBlocked(musician.getIsBlocked())
                .avatarImageUrl(musician.getAvatarImageUrl())
                .headerImageUrl(musician.getHeaderImageUrl())
                .creationDate(musician.getCreationDate())
                .subscriberIds(musician.getSubscriberIds());
    }

    default MusicianResponse toMusicianResponse(Musician musician) {
        return MusicianResponse.newBuilder()
                .setMusicianId(musician.getId().toString())
                .setUserId(musician.getUserId().toString())
                .setNickname(musician.getNickname())
                .setBio(musician.getBio())
                .setIsVerified(musician.getIsVerified())
                .setIsBlocked(musician.getIsBlocked())
                .setAvatarImageUrl(musician.getAvatarImageUrl())
                .setHeaderImageUrl(musician.getHeaderImageUrl())
                .setCreationDate(
                        Timestamp.newBuilder()
                                .setSeconds(musician.getCreationDate().getEpochSecond())
                                .setNanos(musician.getCreationDate().getNano())
                                .build()
                )
                .addAllSubscriberIds(musician.getSubscriberIds().stream()
                        .map(UUID::toString)
                        .toList())
                .build();
    }

    default MusicianResponse toMusicianResponse(MusicianResponseDto dto) {
        return MusicianResponse.newBuilder()
                .setMusicianId(dto.getMusicianId().toString())
                .setUserId(dto.getUserId().toString())
                .setNickname(dto.getNickname())
                .setBio(dto.getBio())
                .setIsVerified(dto.isVerified())
                .setIsBlocked(dto.isBlocked())
                .setAvatarImageUrl(dto.getAvatarImageUrl())
                .setHeaderImageUrl(dto.getHeaderImageUrl())
                .addAllAlbums(dto.getAlbums().stream()
                        .map(album -> com.github.fckng0d.grpc.albumservice.AlbumPreviewResponse.newBuilder()
                                .setAlbumId(album.getAlbumId().toString())
                                .setName(album.getName())
                                .setCoverImageUrl(album.getCoverImageUrl())
                                .setReleaseYear(album.getReleaseYear())
                                .addAllMusicianNicknames(album.getMusicianNicknames())
                                .setIsExplicit(album.getIsExplicit())
                                .build())
                        .toList()
                )
                .setCreationDate(
                        Timestamp.newBuilder()
                                .setSeconds(dto.getCreationDate().getEpochSecond())
                                .setNanos(dto.getCreationDate().getNano())
                                .build()
                )
                .addAllSubscriberIds(dto.getSubscriberIds().stream()
                        .map(UUID::toString)
                        .toList())
                .build();
    }
}


