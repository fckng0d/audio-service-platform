package com.github.fckng0d.userservice.grpc.server;

import com.github.fckng0d.grpc.userservice.UserServiceGrpc;
import com.github.fckng0d.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class UserGrpcServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserService userService;

//    public void
}
