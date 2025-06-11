package com.github.fckng0d.userservice.grpc.client;

import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.musicianservice.CreateMusicianRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianServiceGrpc;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MusicianServiceGrpcClient {

    @GrpcClient("musician-service")
    private MusicianServiceGrpc.MusicianServiceBlockingStub musicianServiceBlockingStub;

    public MusicianResponseDto createMusician(CreateMusicianRequest request) {
        var musicianResponse = musicianServiceBlockingStub.createMusician(request);

        return MusicianResponseDto.builder()
                .musicianId(UUID.fromString(musicianResponse.getMusicianId()))
                .userId(UUID.fromString(musicianResponse.getUserId()))
                .nickname(musicianResponse.getNickname())
                .bio(musicianResponse.getBio())
                .creationDate(Instant.ofEpochSecond(
                        musicianResponse.getCreationDate().getSeconds(),
                        musicianResponse.getCreationDate().getNanos()))
                .isVerified(musicianResponse.getIsVerified())
                .isBlocked(musicianResponse.getIsBlocked())
                .avatarImageUrl(musicianResponse.getAvatarImageUrl())
                .headerImageUrl(musicianResponse.getHeaderImageUrl())
                .build();

    }
}
