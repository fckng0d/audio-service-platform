package com.github.fckng0d.authenticationservice.grpc.server;

import com.github.fckng0d.authenticationservice.mapper.internal.AuthMapper;
import com.github.fckng0d.authenticationservice.service.AuthService;
import com.github.fckng0d.grpc.authenticationservice.AuthResponse;
import com.github.fckng0d.grpc.authenticationservice.AuthServiceGrpc;
import com.github.fckng0d.grpc.authenticationservice.LoginRequest;
import com.github.fckng0d.grpc.authenticationservice.RefreshTokenRequest;
import com.github.fckng0d.grpc.authenticationservice.RegisterRequest;
import io.grpc.stub.StreamObserver;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AuthGrpcServiceImpl extends AuthServiceGrpc.AuthServiceImplBase {
    private final AuthService authService;
    private final AuthMapper authMapper;

    @Override
    public void register(RegisterRequest request, StreamObserver<AuthResponse> responseObserver) {
        var requestDto = authMapper.toRegisterRequestDto(request);
        var authResponseDto = authService.register(requestDto);
        var authResponse = authMapper.toAuthResponse(authResponseDto);

        responseObserver.onNext(authResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void login(LoginRequest request, StreamObserver<AuthResponse> responseObserver) {
        var loginRequestDto = authMapper.toLoginRequestDto(request);
        var authResponseDto = authService.login(loginRequestDto);
        var authResponse = authMapper.toAuthResponse(authResponseDto);

        responseObserver.onNext(authResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void refreshToken(RefreshTokenRequest request, StreamObserver<AuthResponse> responseObserver) {
        String token = request.getRefreshToken();
        var authResponseDto = authService.refresh(token);
        var authResponse = authMapper.toAuthResponse(authResponseDto);

        responseObserver.onNext(authResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void logout(RefreshTokenRequest request, StreamObserver<Empty> responseObserver) {
        String token = request.getRefreshToken();
        authService.revokeToken(token);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
