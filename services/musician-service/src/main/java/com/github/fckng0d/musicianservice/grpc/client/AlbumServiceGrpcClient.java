package com.github.fckng0d.musicianservice.grpc.client;

import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.grpc.albumservice.AlbumByIdRequest;
import com.github.fckng0d.grpc.albumservice.AlbumServiceGrpc;
import com.github.fckng0d.musicianservice.mapper.internal.AlbumMapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlbumServiceGrpcClient {
    @GrpcClient("album-service")
    private AlbumServiceGrpc.AlbumServiceBlockingStub albumServiceBlockingStub;

    private final AlbumMapper albumMapper;

    public AlbumResponseDto createAlbum(CreateAlbumDto createAlbumDto) {
        var createAlbumRequest = albumMapper.toCreateAlbumRequest(createAlbumDto);
        var albumResponse = albumServiceBlockingStub.createAlbum(createAlbumRequest);

        return albumMapper.toAlbumResponseDto(albumResponse);
    }

    public List<UUID> deleteAlbumById(UUID albumId) {
        var albumIdRequest = AlbumByIdRequest.newBuilder()
                .setAlbumId(albumId.toString())
                .build();

        List<UUID> musicianIds = albumServiceBlockingStub.getAllMusicians(albumIdRequest)
                .getMusicianIdsList().stream()
                .map(UUID::fromString)
                .toList();

        albumServiceBlockingStub.deleteAlbum(albumIdRequest);

        return musicianIds;
    }
}
