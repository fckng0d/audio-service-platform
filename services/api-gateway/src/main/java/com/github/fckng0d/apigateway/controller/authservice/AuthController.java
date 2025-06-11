package com.github.fckng0d.apigateway.controller.authservice;

import com.github.fckng0d.apigateway.dto.auth.AccessTokenRequestDto;
import com.github.fckng0d.apigateway.dto.auth.JwtClaimsResponseDto;
import com.github.fckng0d.apigateway.grpc.adapter.auth.AuthGrpcAdapter;
import com.github.fckng0d.apigateway.security.JweTokenUtil;
import com.github.fckng0d.dto.authenticationservice.LoginRequestDto;
import com.github.fckng0d.dto.authenticationservice.RegisterRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping("/adapter-auth")
@RequiredArgsConstructor
public class AuthController {
    @Value("${jwt.access-expiration-ms}")
    private long accessExpiration;

    private final AuthGrpcAdapter authGrpcAdapter;
    private final JweTokenUtil jweTokenUtil;

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody RegisterRequestDto requestDto) {
        return authGrpcAdapter.register(requestDto)
                .map(authResponseDto -> {
                    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponseDto.getRefreshToken())
                            .httpOnly(true)
                            .path("/")
                            .maxAge(Duration.ofMillis(accessExpiration))
                            .sameSite("Strict")
                            .build();

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                            .body(authResponseDto.getAccessToken());
                });
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginRequestDto requestDto) {
        System.out.println("БЛЯТЬ");
        return authGrpcAdapter.login(requestDto)
                .map(authResponseDto -> {
                    ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", authResponseDto.getRefreshToken())
                            .httpOnly(true)
                            .path("/")
                            .maxAge(Duration.ofMillis(accessExpiration))
                            .sameSite("Strict")
                            .build();

                    return ResponseEntity.ok()
                            .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                            .body(authResponseDto.getAccessToken());
                });
    }


    @PostMapping("/refresh-token")
    public Mono<ResponseEntity<String>> refreshToken(@CookieValue("refreshToken") String refreshToken) {
        return authGrpcAdapter.refreshToken(refreshToken)
                .map(authResponseDto -> ResponseEntity.ok()
                        .body(authResponseDto.getAccessToken()));
    }

    @PostMapping("/logout")
    public Mono<ResponseEntity<Void>> logout(@CookieValue(name = "refreshToken", required = false) String refreshToken) {
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .path("/")
                .httpOnly(true)
                .maxAge(0)
                .build();

        if (refreshToken != null) {
            return authGrpcAdapter.logout(refreshToken)
                    .then(Mono.just(ResponseEntity.noContent()
                            .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                            .build()));
        }

        return Mono.just(ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build());
    }

    @GetMapping("/claims")
    public Mono<ResponseEntity<JwtClaimsResponseDto>> getClaimsByAccessToken(
            @RequestBody AccessTokenRequestDto requestDto
    ) {
        var jwtClaimsResponseDto = jweTokenUtil.getClaimsByToken(requestDto.getAccessToken());
        return Mono.just(ResponseEntity.ok().body(jwtClaimsResponseDto));
    }
}
