package com.github.fckng0d.authenticationservice.grpc.client;

import com.github.fckng0d.authenticationservice.exception.grpc.userservice.EmailAlreadyExistsException;
import com.github.fckng0d.authenticationservice.exception.grpc.userservice.UserServiceExceptionHandler;
import com.github.fckng0d.authenticationservice.exception.grpc.userservice.UsernameAlreadyExistsException;
import com.github.fckng0d.authenticationservice.mapper.grpc.UserMapper;
import com.github.fckng0d.dto.userservice.CreateUserRequestDto;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import io.grpc.StatusRuntimeException;
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
    private final UserServiceExceptionHandler userServiceExceptionHandler;

    public UserResponseDto createUser(CreateUserRequestDto requestDto) {
        try {
            var request = userMapper.toCreateUserRequest(requestDto);
            var userResponse = userServiceBlockingStub.createUser(request);
            return userMapper.toUserResponseDto(userResponse);
        } catch (StatusRuntimeException e) {
            throw userServiceExceptionHandler.handle(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
