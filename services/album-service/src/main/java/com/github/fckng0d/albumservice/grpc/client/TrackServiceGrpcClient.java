package com.github.fckng0d.albumservice.grpc.client;

import com.github.fckng0d.albumservice.mapper.grpc.TrackMapper;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import com.github.fckng0d.grpc.trackservice.TrackServiceGrpc;
import com.github.fckng0d.grpc.trackservice.TrackByIdRequest;
import com.github.fckng0d.grpc.trackservice.AlbumIdRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackServiceGrpcClient {
    @GrpcClient("track-service")
    private TrackServiceGrpc.TrackServiceBlockingStub trackServiceBlockingStub;

    private final TrackMapper trackMapper;

    public TrackPreviewResponseDto createTrack(CreateTrackDto createTrackDto) {
        var trackRequest = trackMapper.toTrackRequest(createTrackDto);
        var trackPreviewResponse = trackServiceBlockingStub.createTrack(trackRequest);

        return trackMapper.toTrackPreviewResponseDto(trackPreviewResponse);
    }

    public List<TrackPreviewResponseDto> getTrackPreviewsByAlbumId(UUID albumId) {
        var request = AlbumIdRequest.newBuilder()
                .setAlbumId(albumId.toString())
                .build();

        var response = trackServiceBlockingStub.getTrackPreviewsByAlbumId(request);

        return response.getTracksList().stream()
                .map(trackMapper::toTrackPreviewResponseDto)
                .toList();
    }

    public void deleteTrackById(long trackId) {
        var trackIdRequest = TrackByIdRequest.newBuilder()
                .setTrackId(trackId)
                .build();
        trackServiceBlockingStub.deleteTrack(trackIdRequest);
    }
}
