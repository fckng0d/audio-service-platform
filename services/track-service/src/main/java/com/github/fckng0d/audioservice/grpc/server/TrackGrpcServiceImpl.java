package com.github.fckng0d.audioservice.grpc.server;

import com.github.fckng0d.audioservice.mapper.internal.TrackMapper;
import com.github.fckng0d.audioservice.service.TrackService;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;
import com.github.fckng0d.grpc.trackservice.TrackByIdRequest;
import com.github.fckng0d.grpc.trackservice.TrackPreviewListResponse;
import com.github.fckng0d.grpc.trackservice.TrackPreviewResponse;
import com.github.fckng0d.grpc.trackservice.TrackResponse;
import com.github.fckng0d.grpc.trackservice.TrackServiceGrpc;
import com.github.fckng0d.grpc.trackservice.AlbumIdRequest;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class TrackGrpcServiceImpl extends TrackServiceGrpc.TrackServiceImplBase {
    private final TrackService trackService;
    private final TrackMapper trackMapper;

    @Override
    public void createTrack(CreateTrackRequest request, StreamObserver<TrackPreviewResponse> responseObserver) {
        var crateTrackDto = trackMapper.toCreateTrackDto(request);
        var trackPreviewResponseDto = trackService.createTrack(crateTrackDto);
        var trackPreviewResponse = trackMapper.toTrackPreviewResponse(trackPreviewResponseDto);

        responseObserver.onNext(trackPreviewResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getTrackPreviewsByAlbumId(AlbumIdRequest request, StreamObserver<TrackPreviewListResponse> responseObserver) {
        UUID albumId = UUID.fromString(request.getAlbumId());
        var trackPreviewDtoList = trackService.getAllByAlbumId(albumId);
        var trackPreviewListResponse = TrackPreviewListResponse.newBuilder()
                .addAllTracks(trackPreviewDtoList.stream()
                        .map(trackMapper::toTrackPreviewResponse)
                        .toList())
                .build();

        responseObserver.onNext(trackPreviewListResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getTrackById(TrackByIdRequest request, StreamObserver<TrackResponse> responseObserver) {
        long trackId = request.getTrackId();
        var trackResponseDto = trackService.getTrackById(trackId);
        var trackResponse = trackMapper.toTrackResponse(trackResponseDto);

        responseObserver.onNext(trackResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTrack(TrackByIdRequest request, StreamObserver<Empty> responseObserver) {
        super.deleteTrack(request, responseObserver);
    }

}
