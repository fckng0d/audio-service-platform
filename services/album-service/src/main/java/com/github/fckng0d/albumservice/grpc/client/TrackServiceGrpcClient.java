package com.github.fckng0d.albumservice.grpc.client;

import com.github.fckng0d.albumservice.mapper.grpc.TrackMapper;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import com.github.fckng0d.grpc.trackservice.TrackServiceGrpc;
import com.github.fckng0d.grpc.trackservice.TrackByIdRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrackServiceGrpcClient {
    @GrpcClient("track-service")
    private TrackServiceGrpc.TrackServiceBlockingStub trackServiceBlockingStub;

    private final TrackMapper trackMapper;

    public TrackResponseDto createTrack(CreateTrackDto createTrackDto) {
        var trackRequest = trackMapper.toTrackRequest(createTrackDto);
        var trackResponse = trackServiceBlockingStub.createTrack(trackRequest);

        return trackMapper.toTrackResponseDto(trackResponse);
    }

    public void deleteTrackById(long trackId) {
        var trackIdRequest = TrackByIdRequest.newBuilder()
                .setTrackId(trackId)
                .build();
        trackServiceBlockingStub.deleteTrack(trackIdRequest);
    }
}
