package com.github.fckng0d.userservice.grpc.server;

import com.github.fckng0d.dto.imageservice.UploadImageRequestDto;
import com.github.fckng0d.grpc.userservice.AddMusicianProfileRequest;
import com.github.fckng0d.grpc.userservice.UserProfileByIdRequest;
import com.github.fckng0d.grpc.userservice.UserProfileResponse;
import com.github.fckng0d.grpc.userservice.UserProfileServiceGrpc;
import com.github.fckng0d.grpc.userservice.UploadImageRequest;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.mapper.internal.UserProfileMapper;
import com.github.fckng0d.userservice.service.UserProfileService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserProfileGrpcServiceImpl extends UserProfileServiceGrpc.UserProfileServiceImplBase {
    private final UserProfileService userProfileService;
    private final UserProfileMapper userProfileMapper;

    @Override
    public void getUserProfileById(UserProfileByIdRequest request, StreamObserver<UserProfileResponse> responseObserver) {
        UUID profileId = UUID.fromString(request.getProfileId());
        UserProfile userProfile = userProfileService.getUserProfileById(profileId);
        UserProfileResponse userProfileResponse = userProfileMapper.toUserProfileResponse(userProfile);

        responseObserver.onNext(userProfileResponse);
        responseObserver.onCompleted();
    }

    //TODO:
//    @Override
//    public void addMusicianProfile(AddMusicianProfileRequest request, StreamObserver<Empty> responseObserver) {
//        super.addMusicianProfileId(request, responseObserver);
//    }

    @Override
    public void uploadImage(UploadImageRequest request, StreamObserver<Empty> responseObserver) {
        UUID profileId = UUID.fromString(request.getProfileId());
        UploadImageRequestDto requestDto = UploadImageRequestDto.builder()
                .fileName(request.getFileName())
                .imageFileData(request.getImageFileData().toByteArray())
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
