package com.github.fckng0d.userservice.grpc.client;

import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import com.google.protobuf.ByteString;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.grpc.musicianservice.AlbumIdsResponse;
import com.github.fckng0d.grpc.musicianservice.CreateMusicianRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianByIdRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianByNicknameRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianResponse;
import com.github.fckng0d.grpc.musicianservice.MusicianServiceGrpc;
import com.github.fckng0d.grpc.musicianservice.MusicianSubscriberIdsResponse;
import com.github.fckng0d.grpc.musicianservice.UpdateBioRequest;
import com.github.fckng0d.grpc.musicianservice.UpdateNicknameRequest;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.musicianservice.UploadFileRequest;
import com.github.fckng0d.musicianservice.domain.Musician;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.musicianservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.musicianservice.mapper.grpc.ImageMapper;
import com.github.fckng0d.musicianservice.mapper.internal.AlbumMapper;
import com.github.fckng0d.musicianservice.mapper.internal.MusicianMapper;
import com.github.fckng0d.musicianservice.service.MusicianService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class MusicianServiceGrpcClient {

    @GrpcClient("musician-service")
    private MusicianServiceGrpc.MusicianServiceBlockingStub musicianServiceBlockingStub;

    public MusicianResponseDto createMusician(CreateMusicianRequest request) {
//        UploadFileRequest avatarImageRequest = UploadFileRequest.getDefaultInstance();
//        if (createMusicianDto.getAvatarImage() != null) {
//            avatarImageRequest = UploadFileRequest.newBuilder()
//                    .setFileName(createMusicianDto.getAvatarImage().getFileName())
//                    .setFileData(ByteString.copyFrom(createMusicianDto.getAvatarImage().getFileData()))
//                    .build();
//        }
//
//        UploadFileRequest headerImageRequest = UploadFileRequest.getDefaultInstance();
//        if (createMusicianDto.getHeaderImage() != null) {
//            headerImageRequest = UploadFileRequest.newBuilder()
//                    .setFileName(createMusicianDto.getHeaderImage().getFileName())
//                    .setFileData(ByteString.copyFrom(createMusicianDto.getHeaderImage().getFileData()))
//                    .build();
//        }
//
//        var createRequest = CreateMusicianRequest.newBuilder()
//                .setUserId(createMusicianDto.getUserId().toString())
//                .setNickname(createMusicianDto.getNickname())
//                .setBio(createMusicianDto.getBio())
//                .setAvatarImage(avatarImageRequest)
//                .setHeaderImage(headerImageRequest)
//                .build();

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
                .albumIds(musicianResponse.getAlbumIdsList().stream()
                        .map(UUID::fromString)
                        .toList())
                .build();

    }
}
