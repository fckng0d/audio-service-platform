package com.github.fckng0d.apigateway.controller.musicianservice;

import com.github.fckng0d.apigateway.grpc.adapter.musicianservice.MusicianGrpcAdapter;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/adapter-musician")
@RequiredArgsConstructor
public class MusicianController {
    private final MusicianGrpcAdapter musicianGrpcAdapter;

    @PostMapping("/create")
    public Mono<MusicianResponseDto> createMusician(@RequestBody CreateMusicianDto createMusicianDto) {
        return musicianGrpcAdapter.createMusician(createMusicianDto);
    }

    @GetMapping("/{nickname}")
    public Mono<MusicianResponseDto> getMusicianByNickname(@PathVariable String nickname) {
        return musicianGrpcAdapter.getMusicianByNickname(nickname);
    }

    @PostMapping("/albums/upload")
    public Mono<AlbumResponseDto> uploadAlbum(@RequestBody CreateAlbumDto createAlbumDto) {
        return musicianGrpcAdapter.createAlbum(createAlbumDto);
    }
}
