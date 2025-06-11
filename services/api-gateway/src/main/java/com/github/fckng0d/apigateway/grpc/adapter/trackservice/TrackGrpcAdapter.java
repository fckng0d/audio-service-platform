package com.github.fckng0d.apigateway.grpc.adapter.trackservice;

import com.github.fckng0d.apigateway.mapper.TrackMapper;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.trackservice.TrackServiceGrpc;
import com.github.fckng0d.grpc.trackservice.TrackByIdRequest;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackGrpcAdapter {

    @GrpcClient("track-service")
    private TrackServiceGrpc.TrackServiceBlockingStub trackServiceBlockingStub;

    private final TrackMapper trackMapper;

    public Mono<TrackResponseDto> getTrackById(long trackId) {
        return Mono.fromCallable(() -> {
            var request = TrackByIdRequest.newBuilder()
                    .setTrackId(trackId)
                    .build();
            var response = trackServiceBlockingStub.getTrackById(request);

            return trackMapper.toTrackResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
