package com.github.fckng0d.userservice.grpc.server;

import com.github.fckng0d.grpc.userservice.UserServiceGrpc;
import com.github.fckng0d.grpc.userservice.CreateUserRequest;
import com.github.fckng0d.grpc.userservice.GetUserByUsernameRequest;
import com.github.fckng0d.grpc.userservice.UpdateUsernameRequest;
import com.github.fckng0d.grpc.userservice.UpdatePasswordHashRequest;
import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.dto.user.CreateUserRequestDto;
import com.github.fckng0d.userservice.mapper.internal.UserMapper;
import com.github.fckng0d.userservice.service.UserService;
import com.github.fckng0d.grpc.userservice.UserResponse;
import com.github.fckng0d.grpc.userservice.GetUserByIdRequest;
import com.github.fckng0d.grpc.userservice.UpdateEmailRequest;
import com.github.fckng0d.grpc.userservice.DeleteUserByIdRequest;
import com.github.fckng0d.grpc.userservice.AssignRoleRequest;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;
    private final UserMapper userMapper;

    @Override
    public void getUserById(GetUserByIdRequest request, StreamObserver<UserResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userService.getUserById(userId);
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserByUsername(GetUserByUsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        User user = userService.getUserByUsername(request.getUsername());
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<UserResponse> responseObserver) {
        CreateUserRequestDto createUserRequestDto = userMapper.toCreateUserRequestDto(request);
        User user = userService.createUser(createUserRequestDto);
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUsername(UpdateUsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userService.updateUsername(userId, request.getNewUsername());
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updatePasswordHash(UpdatePasswordHashRequest request, StreamObserver<UserResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userService.updatePasswordHash(userId, request.getPasswordHash());
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updateEmail(UpdateEmailRequest request, StreamObserver<UserResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userService.updateEmail(userId, request.getNewEmail());
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserById(DeleteUserByIdRequest request, StreamObserver<Empty> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        userService.deleteUserById(userId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void assignRole(AssignRoleRequest request, StreamObserver<UserResponse> responseObserver) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userService.assignRole(userId, request.getRoleName());
        UserResponse userResponse = userMapper.toUserResponse(user);

        responseObserver.onNext(userResponse);
        responseObserver.onCompleted();
    }

}
