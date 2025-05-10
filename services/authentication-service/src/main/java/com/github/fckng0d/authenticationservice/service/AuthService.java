package com.github.fckng0d.authenticationservice.service;

import com.github.fckng0d.authenticationservice.domain.RefreshToken;
import com.github.fckng0d.authenticationservice.grpc.client.UserServiceGrpcClient;
import com.github.fckng0d.authenticationservice.repository.RefreshTokenRepository;
import com.github.fckng0d.authenticationservice.security.JweTokenUtil;
import com.github.fckng0d.authenticationservice.security.PasswordEncoderUtil;
import com.github.fckng0d.dto.authenticationservice.LoginRequestDto;
import com.github.fckng0d.dto.authenticationservice.AuthResponseDto;
import com.github.fckng0d.dto.authenticationservice.RegisterRequestDto;
import com.github.fckng0d.dto.userservice.CreateUserRequestDto;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${jwt.refresh-expiration-ms}")
    private long refreshExpiration;

    private final UserServiceGrpcClient userServiceGrpcClient;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JweTokenUtil jweTokenUtil;
    private final PasswordEncoderUtil passwordEncoderUtil;

    public AuthResponseDto register(RegisterRequestDto requestDto) {
        var createUserRequestDto = CreateUserRequestDto.builder()
                .username(requestDto.getUsername())
                .email(requestDto.getEmail())
                .passwordHash(passwordEncoderUtil.encode(requestDto.getPassword()))
                .build();
        var userResponse = userServiceGrpcClient.createUser(createUserRequestDto);

        try {
            return this.generateTokens(userResponse.getUserId(), userResponse.getRoles());
        } catch (Exception e) {
            // TODO: написать исключение
            throw new RuntimeException(e);
        }
    }

    public AuthResponseDto login(LoginRequestDto requestDto) {
        var identifier = requestDto.getIdentifier();
        var userResponse = this.getUserByIdentifier(identifier);

        if (userResponse == null ||
                !passwordEncoderUtil.matches(requestDto.getPassword(), userResponse.getPasswordHash())) {
            // TODO: написать исключение
//            throw new InvalidCredentialsException("Неверный логин или пароль");
        }

        return generateTokens(userResponse.getUserId(), userResponse.getRoles());
    }

    public AuthResponseDto refresh(String token) {
        var refreshToken = refreshTokenRepository.findByToken(token)
                // TODO: написать исключение
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            // TODO: написать исключение
            throw new RuntimeException("Refresh token expired");
        }

        var userResponse = userServiceGrpcClient.getUserById(refreshToken.getUserId());
        return this.generateTokens(userResponse.getUserId(), userResponse.getRoles());
    }

    public void revokeToken(String refreshToken) {
        var token = refreshTokenRepository.findByToken(refreshToken)
                // TODO: написать исключение
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        refreshTokenRepository.delete(token);
    }


    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в 00:00
    public void cleanExpiredTokens() {
        refreshTokenRepository.deleteAllByExpiryDateBefore(Instant.now());
    }

    private UserResponseDto getUserByIdentifier(String identifier) {
//        try {
        return isEmail(identifier)
                ? userServiceGrpcClient.getUserByEmail(identifier)
                : userServiceGrpcClient.getUserByUsername(identifier);
        // TODO: написать исключение
//        } catch (UserNotFoundException e) {
//            return null; // или пробрасывай, если хочешь разную обработку
//        }
    }

    private boolean isEmail(String identifier) {
        return identifier.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private AuthResponseDto generateTokens(UUID userId, Set<String> roles) {
        try {
            var accessTokenString = jweTokenUtil.generateEncryptedToken(userId, roles);
            var refreshTokenString = UUID.randomUUID().toString();

            var refreshToken = RefreshToken.builder()
                    .userId(userId)
                    .token(refreshTokenString)
                    .expiryDate(Instant.now().plusMillis(refreshExpiration))
                    .build();

            refreshTokenRepository.save(refreshToken);

            return new AuthResponseDto(accessTokenString, refreshTokenString);
        } catch (Exception e) {
            // TODO: написать исключение
            throw new RuntimeException("");
        }
    }
}
