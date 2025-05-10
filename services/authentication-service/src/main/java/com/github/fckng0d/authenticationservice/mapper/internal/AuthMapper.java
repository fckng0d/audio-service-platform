package com.github.fckng0d.authenticationservice.mapper.internal;

import com.github.fckng0d.dto.authenticationservice.AuthResponseDto;
import com.github.fckng0d.dto.authenticationservice.LoginRequestDto;
import com.github.fckng0d.dto.authenticationservice.RegisterRequestDto;
import com.github.fckng0d.grpc.authenticationservice.RegisterRequest;
import com.github.fckng0d.grpc.authenticationservice.AuthResponse;
import com.github.fckng0d.grpc.authenticationservice.LoginRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper {
    RegisterRequestDto toRegisterRequestDto(RegisterRequest request);

    AuthResponse toAuthResponse(AuthResponseDto dto);

    LoginRequestDto toLoginRequestDto(LoginRequest request);
}
