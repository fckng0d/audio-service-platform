package com.github.fckng0d.apigateway.controller.userservice;

import com.github.fckng0d.apigateway.dto.userservice.AssignRoleDto;
import com.github.fckng0d.apigateway.dto.userservice.UpdateEmailDto;
import com.github.fckng0d.apigateway.dto.userservice.UpdateUsernameDto;
import com.github.fckng0d.apigateway.grpc.adapter.userservice.UserGrpcAdapter;
import com.github.fckng0d.dto.userservice.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/adapter-user")
@RequiredArgsConstructor
public class UserController {
    private final UserGrpcAdapter userGrpcAdapter;

    @GetMapping
    public Mono<UserResponseDto> getUser(
            @RequestParam(required = false) UUID userId,
            @RequestParam(required = false) String username
    ) {
        if (userId != null) {
            return userGrpcAdapter.getUserById(userId);
        } else if (username != null) {
            return userGrpcAdapter.getUserByUsername(username);
        }
        return Mono.error(new IllegalArgumentException("Must provide either userId or username"));
    }

    @PutMapping("/{userId}/update/username")
    public Mono<Void> updateUsername(@PathVariable UUID userId, UpdateUsernameDto requestDto) {
        if (userId == null) {
            return Mono.error(new IllegalArgumentException("Must provide userId"));
        } else if (requestDto == null || requestDto.getNewUsername().isBlank()) {
            return Mono.error(new IllegalArgumentException("Must provide new username"));
        }
        return userGrpcAdapter.updateUsername(userId, requestDto);
    }

    @PutMapping("/{userId}/update/email")
    public Mono<Void> updateEmail(@PathVariable UUID userId, UpdateEmailDto requestDto) {
        if (userId == null) {
            return Mono.error(new IllegalArgumentException("Must provide userId"));
        } else if (requestDto == null || requestDto.getNewEmail().isBlank()) {
            return Mono.error(new IllegalArgumentException("Must provide new email"));
        }
        return userGrpcAdapter.updateEmail(userId, requestDto);
    }

    @DeleteMapping("/{userId}/delete")
    public Mono<Void> deleteUserById(@PathVariable UUID userId) {
        if (userId == null) {
            return Mono.error(new IllegalArgumentException("Must provide userId"));
        }
        return userGrpcAdapter.deleteUserById(userId);
    }

    @PostMapping("/{userId}/assign-role")
    public Mono<UserResponseDto> assignRole(@PathVariable UUID userId, AssignRoleDto requestDto) {
        if (userId == null) {
            return Mono.error(new IllegalArgumentException("Must provide userId"));
        }
        return userGrpcAdapter.assignRole(userId, requestDto);
    }
}
