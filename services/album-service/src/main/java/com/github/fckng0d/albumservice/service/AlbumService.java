package com.github.fckng0d.albumservice.service;

import com.github.fckng0d.albumservice.domain.Album;
import com.github.fckng0d.albumservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.albumservice.grpc.client.TrackServiceGrpcClient;
import com.github.fckng0d.albumservice.mapper.internal.AlbumMapper;
import com.github.fckng0d.albumservice.repository.AlbumRepository;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final ImageServiceGrpcClient imageServiceGrpcClient;
    private final TrackServiceGrpcClient trackServiceGrpcClient;

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Transactional(readOnly = true)
    protected Album getById(UUID id) {
        return albumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Track not found with id = " + id));
    }

    @Transactional
    public AlbumResponseDto createAlbum(CreateAlbumDto albumDto) {
        String coverImageUrl = Optional.ofNullable(albumDto.getCoverImage())
                .map(imageServiceGrpcClient::uploadImage)
                .orElse(null);

        List<MusicGenre> genres = extractGenres(albumDto.getTracks());
        List<Language> languages = extractLanguages(albumDto.getTracks());
        List<UUID> guestIds = extractTrackGuestIds(albumDto.getTracks(), albumDto.getMusicianIds());

        var album = Album.builder()
                .name(albumDto.getName())
                .musicianIds(albumDto.getMusicianIds())
                .genres(genres)
                .languages(languages)
                .trackGuestIds(guestIds)
                .auditionCount(0L)
                .coverImageUrl(coverImageUrl)
                .albumInFavoritesCount(0)
                .build();


        var savedAlbum = albumRepository.saveAndFlush(album);

        for (CreateTrackDto track : albumDto.getTracks()) {
            track.setAlbumId(savedAlbum.getId());
        }

        List<TrackResponseDto> trackResponseDtoList = albumDto.getTracks().stream()
                .map(trackServiceGrpcClient::createTrack)
                .toList();

        savedAlbum.setTotalDurationSeconds(this.combineTotalDurationSeconds(trackResponseDtoList));
        savedAlbum.setTrackIds(trackResponseDtoList.stream()
                .map(TrackResponseDto::getId)
                .toList());

        savedAlbum = albumRepository.save(savedAlbum);
        return albumMapper.toAlbumResponseDto(savedAlbum);
    }

    @Transactional
    public void deleteAlbumById(UUID albumId) {
        Album album = this.getById(albumId);

        album.getTrackIds().forEach(trackServiceGrpcClient::deleteTrackById);

        albumRepository.delete(album);
    }

    @Transactional
    public List<UUID> getAllMusicians(UUID albumId) {
        Album album = this.getById(albumId);
        return album.getMusicianIds();
    }

    private List<MusicGenre> extractGenres(List<CreateTrackDto> trackDtoList) {
        return trackDtoList.stream()
                .flatMap(track -> track.getGenres().stream())
                .distinct()
                .toList();
    }

    private List<Language> extractLanguages(List<CreateTrackDto> trackDtoList) {
        return trackDtoList.stream()
                .flatMap(track -> track.getLanguages().stream())
                .distinct()
                .toList();
    }

    private List<UUID> extractTrackGuestIds(List<CreateTrackDto> trackDtoList, List<UUID> albumMusicianIds) {
        Set<UUID> albumMusicianIdSet = new HashSet<>(albumMusicianIds);
        return trackDtoList.stream()
                .flatMap(track -> track.getMusicianIds().stream())
                .filter(musicianId -> !albumMusicianIdSet.contains(musicianId))
                .distinct()
                .toList();
    }

    private Integer combineTotalDurationSeconds(List<TrackResponseDto> trackResponseDtoList) {
        return trackResponseDtoList.stream()
                .mapToInt(TrackResponseDto::getDurationSeconds)
                .sum();
    }
}
