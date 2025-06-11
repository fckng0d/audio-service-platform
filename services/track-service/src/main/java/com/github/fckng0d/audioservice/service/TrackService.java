package com.github.fckng0d.audioservice.service;

import com.github.fckng0d.audioservice.domain.Track;
import com.github.fckng0d.audioservice.grpc.client.StorageServiceGrpcClient;
import com.github.fckng0d.audioservice.mapper.internal.TrackMapper;
import com.github.fckng0d.audioservice.repository.TrackRepository;
import com.github.fckng0d.dto.storageservice.AudioResponseDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TrackService {
    private final StorageServiceGrpcClient storageServiceGrpcClient;

    private final TrackRepository trackRepository;
    private final TrackMapper trackMapper;

    @Transactional(readOnly = true)
    protected Track getById(long id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found"));
    }

    @Transactional
    public TrackPreviewResponseDto createTrack(CreateTrackDto createTrackDto) {
        String coverImageUrl = Optional.ofNullable(createTrackDto.getCoverImage())
                .map(storageServiceGrpcClient::uploadImage)
                .orElse(null);

        String trackUrl = Optional.ofNullable(createTrackDto.getTrackFile())
                .map(track -> storageServiceGrpcClient.uploadAudio(track).getAudioUrl())
                .orElse(null);

        Track track = trackMapper.toTrack(createTrackDto);
        track.setCoverImageUrl(coverImageUrl);
        track.setTrackUrl(trackUrl);

        Track savedTrack = trackRepository.save(track);
        return trackMapper.toTrackPreviewResponseDto(savedTrack);
    }

    @Transactional
    public TrackResponseDto getTrackById(long trackId) {
        Track track = this.getById(trackId);
        return trackMapper.toTrackResponseDto(track);
    }

    @Transactional
    public List<TrackPreviewResponseDto> getAllByAlbumId(UUID albumId) {
        List<Track> tracks = trackRepository.findAllByAlbumId(albumId);

        return tracks.stream()
                .map(trackMapper::toTrackPreviewResponseDto)
                .toList();
    }

    public void deleteTrackById(long trackId) {
        // TODO: удаление во всех альбомах
    }
}
