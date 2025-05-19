package com.github.fckng0d.apigateway.grpc.adapter.userservice;

import com.github.fckng0d.apigateway.dto.userservice.AssignRoleDto;
import com.github.fckng0d.apigateway.dto.userservice.UpdateEmailDto;
import com.github.fckng0d.apigateway.dto.userservice.UpdateUsernameDto;
import com.github.fckng0d.apigateway.mapper.UserMapper;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import com.github.fckng0d.grpc.userservice.UserServiceGrpc;
import com.github.fckng0d.grpc.userservice.GetUserByIdRequest;
import com.github.fckng0d.grpc.userservice.GetUserByUsernameRequest;
import com.github.fckng0d.grpc.userservice.UpdateUsernameRequest;
import com.github.fckng0d.grpc.userservice.UpdateEmailRequest;
import com.github.fckng0d.grpc.userservice.DeleteUserByIdRequest;
import com.github.fckng0d.grpc.userservice.AssignRoleRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserGrpcAdapter {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    private final UserMapper userMapper;

    public Mono<UserResponseDto> getUserById(UUID userId) {
        return Mono.fromCallable(() -> {
            var grpcRequest = GetUserByIdRequest.newBuilder()
                    .setUserId(userId.toString())
                    .build();
            var grpcResponse = userServiceBlockingStub.getUserById(grpcRequest);
            return userMapper.toUserResponseDto(grpcResponse);
        }).subscribeOn(Schedulers.boundedElastic());
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }

    public Mono<UserResponseDto> getUserByUsername(String username) {
        return Mono.fromCallable(() -> {
            var grpcRequest = GetUserByUsernameRequest.newBuilder()
                    .setUsername(username)
                    .build();
            var grpcResponse = userServiceBlockingStub.getUserByUsername(grpcRequest);
            return userMapper.toUserResponseDto(grpcResponse);
        }).subscribeOn(Schedulers.boundedElastic());
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }

    public Mono<Void> updateUsername(UUID userId, UpdateUsernameDto requestDto) {
        return Mono.fromRunnable(() -> {
            var grpcRequest = UpdateUsernameRequest.newBuilder()
                    .setUserId(userId.toString())
                    .setNewUsername(requestDto.getNewUsername())
                    .build();
            userServiceBlockingStub.updateUsername(grpcRequest);
        }).subscribeOn(Schedulers.boundedElastic()).then();
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }

    public Mono<Void> updateEmail(UUID userId, UpdateEmailDto requestDto) {
        return Mono.fromRunnable(() -> {
            var grpcRequest = UpdateEmailRequest.newBuilder()
                    .setUserId(userId.toString())
                    .setNewEmail(requestDto.getNewEmail())
                    .build();
            userServiceBlockingStub.updateEmail(grpcRequest);
        }).subscribeOn(Schedulers.boundedElastic()).then();
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }

    public Mono<Void> deleteUserById(UUID userId) {
        return Mono.fromRunnable(() -> {
            var grpcRequest = DeleteUserByIdRequest.newBuilder()
                    .setUserId(userId.toString())
                    .build();
            userServiceBlockingStub.deleteUserById(grpcRequest);
        }).subscribeOn(Schedulers.boundedElastic()).then();
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }

    public Mono<UserResponseDto> assignRole(UUID userId, AssignRoleDto requestDto) {
        return Mono.fromCallable(() -> {
            var grpcRequest = AssignRoleRequest.newBuilder()
                    .setUserId(userId.toString())
                    .setRoleName(requestDto.getRoleName())
                    .build();
            var grpcResponse = userServiceBlockingStub.assignRole(grpcRequest);
            return userMapper.toUserResponseDto(grpcResponse);
        }).subscribeOn(Schedulers.boundedElastic());
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }
}
