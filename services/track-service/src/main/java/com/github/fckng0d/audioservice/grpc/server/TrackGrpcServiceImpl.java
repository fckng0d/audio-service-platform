package com.github.fckng0d.audioservice.grpc.server;

import com.github.fckng0d.audioservice.mapper.internal.TrackMapper;
import com.github.fckng0d.audioservice.service.TrackService;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;
import com.github.fckng0d.grpc.trackservice.TrackByIdRequest;
import com.github.fckng0d.grpc.trackservice.TrackResponse;
import com.github.fckng0d.grpc.trackservice.TrackServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class TrackGrpcServiceImpl extends TrackServiceGrpc.TrackServiceImplBase {
    private final TrackService trackService;
    private final TrackMapper trackMapper;

    @Override
    public void createTrack(CreateTrackRequest request, StreamObserver<TrackResponse> responseObserver) {
        var crateTrackDto = trackMapper.toCreateTrackDto(request);
        var trackResponseDto = trackService.createTrack(crateTrackDto);
        var trackResponse = trackMapper.toTrackResponse(trackResponseDto);

        responseObserver.onNext(trackResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteTrack(TrackByIdRequest request, StreamObserver<Empty> responseObserver) {
        super.deleteTrack(request, responseObserver);
    }

    @Override
    public void getTrackById(TrackByIdRequest request, StreamObserver<TrackResponse> responseObserver) {
        super.getTrackById(request, responseObserver);
    }
}
