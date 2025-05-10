package com.github.fckng0d.authenticationservice.grpc.client;

import com.github.fckng0d.authenticationservice.mapper.grpc.UserMapper;
import com.github.fckng0d.dto.userservice.CreateUserRequestDto;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.userservice.UserServiceGrpc;
import com.github.fckng0d.grpc.userservice.GetUserByUsernameRequest;
import com.github.fckng0d.grpc.userservice.GetUserByIdRequest;
import com.github.fckng0d.grpc.userservice.GetUserByEmailRequest;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceGrpcClient {
    @GrpcClient("user-service")
    private UserServiceGrpc.UserServiceBlockingStub userServiceBlockingStub;

    private final UserMapper userMapper;

    public UserResponseDto createUser(CreateUserRequestDto requestDto) {
        var request = userMapper.toCreateUserRequest(requestDto);
        var userResponse = userServiceBlockingStub.createUser(request);

        return userMapper.toUserResponseDto(userResponse);
    }

    public UserResponseDto getUserById(UUID userId) {
        var userRequest = GetUserByIdRequest.newBuilder()
                .setUserId(userId.toString())
                .build();
        var userResponse = userServiceBlockingStub.getUserById(userRequest);
        return userMapper.toUserResponseDto(userResponse);
    }

    public UserResponseDto getUserByUsername(String username) {
        var userRequest = GetUserByUsernameRequest.newBuilder()
                .setUsername(username)
                .build();
        var userResponse = userServiceBlockingStub.getUserByUsername(userRequest);
        return userMapper.toUserResponseDto(userResponse);
    }

    public UserResponseDto getUserByEmail(String email) {
        var userRequest = GetUserByEmailRequest.newBuilder()
                .setEmail(email)
                .build();
        var userResponse = userServiceBlockingStub.getUserByEmail(userRequest);
        return userMapper.toUserResponseDto(userResponse);
    }

}
