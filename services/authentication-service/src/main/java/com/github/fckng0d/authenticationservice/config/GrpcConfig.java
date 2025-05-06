package com.github.fckng0d.authenticationservice.config;

import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {
    @Bean
    public UserServiceGrpc.UserServiceBlockingStub userServiceStub(
            @GrpcClient("user-service") Channel channel) {
        return UserServiceGrpc.newBlockingStub(channel);
    }
}
