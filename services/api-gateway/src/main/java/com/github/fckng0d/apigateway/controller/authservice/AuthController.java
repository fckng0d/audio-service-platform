package com.github.fckng0d.apigateway.controller.authservice;

import com.github.fckng0d.apigateway.dto.auth.RefreshTokenRequestDto;
import com.github.fckng0d.apigateway.grpc.adapter.auth.AuthServiceGrpcAdapter;
import com.github.fckng0d.dto.authenticationservice.AuthResponseDto;
import com.github.fckng0d.dto.authenticationservice.LoginRequestDto;
import com.github.fckng0d.dto.authenticationservice.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/adapter-auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceGrpcAdapter authServiceGrpcAdapter;

    @PostMapping("/register")
    public Mono<AuthResponseDto> register(@RequestBody RegisterRequestDto requestDto) {
        return authServiceGrpcAdapter.register(requestDto);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return authServiceGrpcAdapter.login(requestDto);
    }

    @PostMapping("/refresh-token")
    public Mono<AuthResponseDto> refreshToken(@RequestBody RefreshTokenRequestDto requestDto) {
        return authServiceGrpcAdapter.refreshToken(requestDto.getToken());
    }

    @PostMapping("/logout")
    public Mono<Void> logout(@RequestBody RefreshTokenRequestDto requestDto) {
        return authServiceGrpcAdapter.logout(requestDto.getToken());
    }
}
