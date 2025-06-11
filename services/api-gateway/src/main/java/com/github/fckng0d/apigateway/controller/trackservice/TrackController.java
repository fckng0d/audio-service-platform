package com.github.fckng0d.apigateway.controller.trackservice;

import com.github.fckng0d.apigateway.grpc.adapter.trackservice.TrackGrpcAdapter;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/adapter-track")
@RequiredArgsConstructor
public class TrackController {
    private final TrackGrpcAdapter trackGrpcAdapter;

    @GetMapping("/{trackId}")
    public Mono<TrackResponseDto> getTrackById(@PathVariable long trackId) {
        return trackGrpcAdapter.getTrackById(trackId);
    }
}
