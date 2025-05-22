package com.github.fckng0d.audioservice.service;

import com.github.fckng0d.audioservice.domain.Track;
import com.github.fckng0d.audioservice.grpc.client.StorageServiceGrpcClient;
import com.github.fckng0d.audioservice.mapper.internal.TrackMapper;
import com.github.fckng0d.audioservice.repository.TrackRepository;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final StorageServiceGrpcClient storageServiceGrpcClient;

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    @Transactional
    public TrackResponseDto createTrack(CreateTrackDto createTrackDto) {
        String coverImageUrl = Optional.ofNullable(createTrackDto.getCoverImage())
                .map(storageServiceGrpcClient::uploadImage)
                .orElse(null);

        String trackUrl = Optional.ofNullable(createTrackDto.getTrackFile())
                .map(storageServiceGrpcClient::uploadAudio)
                .orElse(null);

        Track track = trackMapper.toTrack(createTrackDto);
        track.setCoverImageUrl(coverImageUrl);
        track.setTrackUrl(trackUrl);

        Track savedTrack = trackRepository.save(track);
        return trackMapper.toTrackResponseDto(savedTrack);
    }

    public void deleteTrackById(long trackId) {
        // TODO: удаление во всех альбомах
    }
}
