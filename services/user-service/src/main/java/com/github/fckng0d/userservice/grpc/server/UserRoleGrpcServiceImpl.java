package com.github.fckng0d.userservice.grpc.server;

import com.github.fckng0d.grpc.userservice.CreateRoleRequest;
import com.github.fckng0d.grpc.userservice.GetRoleByNameRequest;
import com.github.fckng0d.grpc.userservice.UserRoleResponse;
import com.github.fckng0d.grpc.userservice.UserRoleServiceGrpc;
import com.github.fckng0d.grpc.userservice.UsersByRoleNameResponse;
import com.github.fckng0d.grpc.userservice.GetUsersByRoleNameRequest;
import com.github.fckng0d.grpc.userservice.UpdateUserRoleRequest;
import com.github.fckng0d.grpc.userservice.DeleteUserRoleByNameRequest;
import com.github.fckng0d.userservice.dto.role.UpdateUserRoleRequestDto;
import com.google.protobuf.Empty;
import com.github.fckng0d.userservice.domain.UserRole;
import com.github.fckng0d.userservice.dto.role.CreateUserRoleRequestDto;
import com.github.fckng0d.userservice.mapper.UserRoleMapper;
import com.github.fckng0d.userservice.service.UserRoleService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserRoleGrpcServiceImpl extends UserRoleServiceGrpc.UserRoleServiceImplBase {
    private final UserRoleService userRoleService;
    private final UserRoleMapper userRoleMapper;

    public void getRoleByName(GetRoleByNameRequest request, StreamObserver<UserRoleResponse> responseObserver) {
        UserRole role = userRoleService.getRoleByName(request.getRoleName());
        UserRoleResponse userRoleResponse = userRoleMapper.toUserRoleResponse(role);

        responseObserver.onNext(userRoleResponse);
        responseObserver.onCompleted();
    }

    public void createRole (CreateRoleRequest request, StreamObserver<UserRoleResponse> responseObserver) {
        CreateUserRoleRequestDto createUserRoleRequestDto = userRoleMapper.toCreateUserRoleDto(request);
        UserRole role = userRoleService.createRole(createUserRoleRequestDto);
        UserRoleResponse userRoleResponse = userRoleMapper.toUserRoleResponse(role);

        responseObserver.onNext(userRoleResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getUsersByRoleName(GetUsersByRoleNameRequest request, StreamObserver<UsersByRoleNameResponse> responseObserver) {
        UserRole role = userRoleService.getRoleByName(request.getRoleName());
        UsersByRoleNameResponse usersResponse = userRoleMapper.toUsersByRoleNameResponse(role);

        responseObserver.onNext(usersResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUserRole(UpdateUserRoleRequest request, StreamObserver<Empty> responseObserver) {
        UpdateUserRoleRequestDto updateUserRoleRequestDto = userRoleMapper.toUpdateUserRoleRequestDto(request);

        userRoleService.updateUserRole(updateUserRoleRequestDto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteUserRoleByName(DeleteUserRoleByNameRequest request, StreamObserver<Empty> responseObserver) {
        userRoleService.deleteUserRoleByName(request.getRoleName());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
