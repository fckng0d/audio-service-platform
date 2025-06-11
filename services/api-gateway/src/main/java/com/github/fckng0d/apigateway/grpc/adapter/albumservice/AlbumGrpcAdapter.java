package com.github.fckng0d.apigateway.grpc.adapter.albumservice;

import com.github.fckng0d.apigateway.mapper.AlbumMapper;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.albumservice.AlbumServiceGrpc;
import com.github.fckng0d.grpc.albumservice.AlbumByIdRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumGrpcAdapter {

    @GrpcClient("album-service")
    private AlbumServiceGrpc.AlbumServiceBlockingStub albumServiceBlockingStub;

    private final AlbumMapper albumMapper;

    public Mono<AlbumResponseDto> getAlbumById(UUID albumId) {
        return Mono.fromCallable(() -> {
            var request = AlbumByIdRequest.newBuilder()
                    .setAlbumId(albumId.toString())
                    .build();
            var response = albumServiceBlockingStub.getAlbumById(request);

            return albumMapper.toAlbumResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
