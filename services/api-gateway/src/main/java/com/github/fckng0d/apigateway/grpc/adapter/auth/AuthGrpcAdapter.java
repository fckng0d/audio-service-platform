package com.github.fckng0d.apigateway.grpc.adapter.auth;

import com.github.fckng0d.apigateway.grpc.exception.authservice.AuthServiceExceptionHandler;
import com.github.fckng0d.apigateway.mapper.AuthMapper;
import com.github.fckng0d.dto.authenticationservice.AuthResponseDto;
import com.github.fckng0d.dto.authenticationservice.LoginRequestDto;
import com.github.fckng0d.dto.authenticationservice.RegisterRequestDto;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.authenticationservice.AuthServiceGrpc;
import com.github.fckng0d.grpc.authenticationservice.RefreshTokenRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class AuthGrpcAdapter {

    @GrpcClient("authentication-service")
    private AuthServiceGrpc.AuthServiceBlockingStub authServiceBlockingStub;

    private final AuthMapper authMapper;
    private final AuthServiceExceptionHandler authServiceExceptionHandler;

    public Mono<AuthResponseDto> register(RegisterRequestDto requestDto) {
        return Mono.fromCallable(() -> {
                    var grpcRequest = authMapper.toRegisterRequest(requestDto);
                    var grpcResponse = authServiceBlockingStub.register(grpcRequest);
                    return authMapper.toAuthResponseDto(grpcResponse);
                }).subscribeOn(Schedulers.boundedElastic())
                .onErrorMap(StatusRuntimeException.class, authServiceExceptionHandler::handle);
    }

    public Mono<AuthResponseDto> login(LoginRequestDto requestDto) {
        return Mono.fromCallable(() -> {
            var grpcRequest = authMapper.toLoginRequest(requestDto);
            var grpcResponse = authServiceBlockingStub.login(grpcRequest);
            return authMapper.toAuthResponseDto(grpcResponse);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<AuthResponseDto> refreshToken(String token) {
        return Mono.fromCallable(() -> {
            var grpcRequest = RefreshTokenRequest.newBuilder()
                    .setRefreshToken(token)
                    .build();
            var grpcResponse = authServiceBlockingStub.refreshToken(grpcRequest);
            return authMapper.toAuthResponseDto(grpcResponse);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> logout(String token) {
        return Mono.fromRunnable(() -> {
            var request = RefreshTokenRequest.newBuilder()
                    .setRefreshToken(token)
                    .build();
            authServiceBlockingStub.logout(request);
        }).subscribeOn(Schedulers.boundedElastic()).then();
    }
}
