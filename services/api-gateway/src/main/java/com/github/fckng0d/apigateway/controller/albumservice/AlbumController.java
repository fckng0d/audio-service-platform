package com.github.fckng0d.apigateway.controller.albumservice;

import com.github.fckng0d.apigateway.grpc.adapter.albumservice.AlbumGrpcAdapter;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/adapter-album")
@RequiredArgsConstructor
public class AlbumController {
    private final AlbumGrpcAdapter albumGrpcAdapter;

    @GetMapping("/{albumId}")
    public Mono<AlbumResponseDto> getAlbumById(@PathVariable UUID albumId) {
        return albumGrpcAdapter.getAlbumById(albumId);
    }
}
