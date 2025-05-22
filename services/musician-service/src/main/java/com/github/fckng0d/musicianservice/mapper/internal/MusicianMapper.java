package com.github.fckng0d.musicianservice.mapper.internal;

import com.github.fckng0d.musicianservice.domain.Musician;
import com.google.protobuf.Timestamp;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import com.github.fckng0d.grpc.musicianservice.MusicianResponse;
import org.mapstruct.MappingTarget;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface MusicianMapper {

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
                .addAllAlbumIds(
                        musician.getAlbumIds().stream()
                                .map(UUID::toString)
                                .toList()
                )
                .setCreationDate(
                        Timestamp.newBuilder()
                                .setSeconds(musician.getCreationDate().getEpochSecond())
                                .setNanos(musician.getCreationDate().getNano())
                                .build()
                )
                .build();
    }
}


