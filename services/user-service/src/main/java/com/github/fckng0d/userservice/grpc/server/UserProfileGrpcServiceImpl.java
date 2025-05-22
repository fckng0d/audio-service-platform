package com.github.fckng0d.userservice.grpc.server;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.grpc.userservice.UploadImageRequest;
import com.github.fckng0d.grpc.userservice.UserProfileByIdRequest;
import com.github.fckng0d.grpc.userservice.UserProfileResponse;
import com.github.fckng0d.grpc.userservice.UserProfileServiceGrpc;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.userservice.mapper.internal.UserProfileMapper;
import com.github.fckng0d.userservice.service.UserProfileService;
import com.github.fckng0d.grpc.musicianservice.CreateMusicianRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianResponse;
import com.google.protobuf.Empty;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserProfileGrpcServiceImpl extends UserProfileServiceGrpc.UserProfileServiceImplBase {
    private final ImageServiceGrpcClient imageServiceGrpcClient;

    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @Override
    public void getUserProfileById(UserProfileByIdRequest request, StreamObserver<UserProfileResponse> responseObserver) {
        UUID profileId = UUID.fromString(request.getProfileId());
        UserProfile userProfile = userProfileService.getUserProfileById(profileId);

        var registrationDate = Timestamp.newBuilder()
                .setSeconds(userProfile.getRegistrationDate().getEpochSecond())
                .setNanos(userProfile.getRegistrationDate().getNano())
                .build();

        UserProfileResponse userProfileResponse = UserProfileResponse.newBuilder()
                .setProfileId(userProfile.getId().toString())
                .setMusicianProfileId(userProfile.getMusicianProfileId().toString())
                .setRegistrationDate(registrationDate)
                .setImageUrl(userProfile.getImageUrl())
                .build();

        responseObserver.onNext(userProfileResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void createMusicianProfile(CreateMusicianRequest request, StreamObserver<MusicianResponse> responseObserver) {
        var musicianResponseDto = userProfileService.createMusicianProfile(request);

        var musicianResponse = MusicianResponse.newBuilder()
                .setMusicianId(musicianResponseDto.getMusicianId().toString())
                .setUserId(musicianResponseDto.getUserId().toString())
                .setNickname(musicianResponseDto.getNickname())
                .setBio(musicianResponseDto.getBio())
                .setIsVerified(musicianResponseDto.isVerified())
                .setIsBlocked(musicianResponseDto.isBlocked())
                .setAvatarImageUrl(musicianResponseDto.getAvatarImageUrl())
                .setHeaderImageUrl(musicianResponseDto.getHeaderImageUrl())
                .addAllAlbumIds(
                        musicianResponseDto.getAlbumIds().stream()
                                .map(UUID::toString)
                                .toList()
                )
                .setCreationDate(
                        Timestamp.newBuilder()
                                .setSeconds(musicianResponseDto.getCreationDate().getEpochSecond())
                                .setNanos(musicianResponseDto.getCreationDate().getNano())
                                .build()
                )
                .build();

        responseObserver.onNext(musicianResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void uploadImage(UploadImageRequest request, StreamObserver<Empty> responseObserver) {
        UUID profileId = UUID.fromString(request.getProfileId());
        UploadFileDto requestDto = UploadFileDto.builder()
                .fileName(request.getFileName())
                .fileData(request.getImageFileData().toByteArray())
                .build();

        userProfileService.uploadProfileImage(profileId, requestDto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteProfileImage(UserProfileByIdRequest request, StreamObserver<Empty> responseObserver) {
        UUID profileId = UUID.fromString(request.getProfileId());
        userProfileService.deleteProfileImage(profileId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
