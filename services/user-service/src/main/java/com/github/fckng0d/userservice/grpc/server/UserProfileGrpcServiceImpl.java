package com.github.fckng0d.userservice.grpc.server;

import com.github.fckng0d.grpc.userservice.UserProfileServiceGrpc;
import com.github.fckng0d.grpc.userservice.GetUserProfileByIdRequest;
import com.github.fckng0d.grpc.userservice.UserProfileResponse;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.mapper.internal.UserProfileMapper;
import com.github.fckng0d.userservice.service.UserProfileService;
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
    public void getUserProfileById(GetUserProfileByIdRequest request, StreamObserver<UserProfileResponse> responseObserver) {
        UUID profileId = UUID.fromString(request.getProfileId());
        UserProfile userProfile = userProfileService.getUserProfileById(profileId);
        UserProfileResponse userProfileResponse = userProfileMapper.toUserProfileResponse(userProfile);

        responseObserver.onNext(userProfileResponse);
        responseObserver.onCompleted();
    }

}
