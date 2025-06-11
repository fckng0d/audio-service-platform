package com.github.fckng0d.apigateway.grpc.adapter.userservice;

import com.github.fckng0d.apigateway.dto.userservice.CreateRoleRequestDto;
import com.github.fckng0d.apigateway.dto.userservice.UsersIdsByRoleNameRequestDto;
import com.github.fckng0d.apigateway.dto.userservice.UsersIdsByRoleNameResponseDto;
import com.github.fckng0d.apigateway.mapper.userservice.UserRoleMapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.userservice.UserRoleServiceGrpc;
import com.github.fckng0d.grpc.userservice.GetUsersByRoleNameRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserRoleGrpcAdapter {
    @GrpcClient("user-service")
    private UserRoleServiceGrpc.UserRoleServiceBlockingStub roleServiceBlockingStub;

    private final UserRoleMapper userRoleMapper;

    public Mono<Void> createRole(CreateRoleRequestDto requestDto) {
        return Mono.fromRunnable(() -> {
            var grpcRequest = userRoleMapper.toCreateRoleRequest(requestDto);
            roleServiceBlockingStub.createRole(grpcRequest);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }

    public Mono<UsersIdsByRoleNameResponseDto> getUsersByRoleName(UsersIdsByRoleNameRequestDto requestDto) {
        return Mono.fromCallable(() -> {
            var grpcRequest = GetUsersByRoleNameRequest.newBuilder()
                    .setRoleName(requestDto.getRoleName())
                    .build();
            var grpcResponse = roleServiceBlockingStub.getUsersByRoleName(grpcRequest);

            return new UsersIdsByRoleNameResponseDto(
                    grpcResponse.getUserIdsList().stream()
                            .map(UUID::fromString)
                            .collect(Collectors.toList())
            );
        }).subscribeOn(Schedulers.boundedElastic());
//                .onErrorMap(StatusRuntimeException.class, userServiceExceptionHandler::handle);
    }

    // TODO:
    //      getUsersByRoleName,
    //      deleteUserRoleByName,
    //      updateUserRole
}
