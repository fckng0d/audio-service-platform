package com.github.fckng0d.apigateway.mapper;

import com.github.fckng0d.dto.authenticationservice.AuthResponseDto;
import com.github.fckng0d.dto.authenticationservice.LoginRequestDto;
import com.github.fckng0d.dto.authenticationservice.RegisterRequestDto;
import com.github.fckng0d.grpc.authenticationservice.RegisterRequest;
import com.github.fckng0d.grpc.authenticationservice.LoginRequest;
import com.github.fckng0d.grpc.authenticationservice.AuthResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    RegisterRequest toRegisterRequest(RegisterRequestDto dto);

    AuthResponseDto toAuthResponseDto(AuthResponse response);

    LoginRequest toLoginRequest(LoginRequestDto dto);
}