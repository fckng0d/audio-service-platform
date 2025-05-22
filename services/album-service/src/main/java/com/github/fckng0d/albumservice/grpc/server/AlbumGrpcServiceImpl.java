package com.github.fckng0d.albumservice.grpc.server;

import com.github.fckng0d.albumservice.mapper.internal.AlbumMapper;
import com.github.fckng0d.albumservice.service.AlbumService;
import com.github.fckng0d.grpc.albumservice.AlbumByIdRequest;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.albumservice.AlbumServiceGrpc;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.grpc.albumservice.AlbumMusiciansResponse;
import com.github.fckng0d.grpc.trackservice.TrackServiceGrpc;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class AlbumGrpcServiceImpl extends AlbumServiceGrpc.AlbumServiceImplBase {
    private final AlbumService albumService;
    private final AlbumMapper albumMapper;

    @Override
    public void createAlbum(CreateAlbumRequest request, StreamObserver<AlbumResponse> responseObserver) {
        var createAlbumDto = albumMapper.toCreateAlbumDto(request);
        var albumResponseDto = albumService.createAlbum(createAlbumDto);
        var albumResponse = albumMapper.toAlbumResponse(albumResponseDto);

        responseObserver.onNext(albumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAlbum(AlbumByIdRequest request, StreamObserver<Empty> responseObserver) {
        UUID albumId = UUID.fromString(request.getAlbumId());
        albumService.deleteAlbumById(albumId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getAllMusicians(AlbumByIdRequest request, StreamObserver<AlbumMusiciansResponse> responseObserver) {
        UUID albumId = UUID.fromString(request.getAlbumId());

        List<UUID> musicianIds = albumService.getAllMusicians(albumId);
        var response = AlbumMusiciansResponse.newBuilder()
                .addAllMusicianIds(musicianIds.stream()
                        .map(UUID::toString)
                        .toList())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
