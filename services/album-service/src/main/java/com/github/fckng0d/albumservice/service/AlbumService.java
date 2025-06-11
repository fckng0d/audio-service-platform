package com.github.fckng0d.albumservice.service;

import com.github.fckng0d.albumservice.domain.Album;
import com.github.fckng0d.albumservice.grpc.client.StorageServiceGrpcClient;
import com.github.fckng0d.albumservice.grpc.client.TrackServiceGrpcClient;
import com.github.fckng0d.albumservice.mapper.internal.AlbumMapper;
import com.github.fckng0d.albumservice.repository.AlbumRepository;
import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AlbumService {
    private final StorageServiceGrpcClient storageServiceGrpcClient;
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
                .map(storageServiceGrpcClient::uploadImage)
                .orElse(null);

        List<MusicGenre> genres = this.extractGenres(albumDto.getTracks());
        List<Language> languages = this.extractLanguages(albumDto.getTracks());
        List<String> guestNicknames = this.extractTrackGuestNicknames(albumDto.getTracks(), albumDto.getMusicianNicknames());

        Album album = albumMapper.toAlbumBuilder(albumDto)
                .coverImageUrl(coverImageUrl)
                .genres(genres)
                .languages(languages)
                .trackGuestNicknames(guestNicknames)
                .build();


        var savedAlbum = albumRepository.saveAndFlush(album);

        for (CreateTrackDto track : albumDto.getTracks()) {
            track.setAlbumId(savedAlbum.getId());
        }

        List<TrackPreviewResponseDto> trackPreviewResponseDtoList = albumDto.getTracks().stream()
                .map(trackServiceGrpcClient::createTrack)
                .toList();

        savedAlbum.setTotalDurationSeconds(this.combineTotalDurationSeconds(trackPreviewResponseDtoList));
        savedAlbum.setTrackIds(trackPreviewResponseDtoList.stream()
                .map(TrackPreviewResponseDto::getId)
                .toList());

        savedAlbum = albumRepository.save(savedAlbum);

        return albumMapper.toAlbumResponseDto(savedAlbum, trackPreviewResponseDtoList);
    }

    @Transactional
    public AlbumResponseDto getAlbumById(UUID albumId) {
        Album album = this.getById(albumId);
        var trackPreviewDtoList = trackServiceGrpcClient.getTrackPreviewsByAlbumId(albumId);

        return albumMapper.toAlbumResponseDto(album, trackPreviewDtoList);
    }

    @Transactional
    public void deleteAlbumById(UUID albumId) {
        Album album = this.getById(albumId);

        album.getTrackIds().forEach(trackServiceGrpcClient::deleteTrackById);

        albumRepository.delete(album);
    }

    @Transactional
    public List<AlbumPreviewResponseDto> getAllByMusicianNickname(String nickname) {
        List<Album> albums = albumRepository.findAllByMusicianNickname(nickname);
        return albumMapper.toAlbumPreviewResponseDtoList(albums);
    }

    @Transactional
    public List<AlbumPreviewResponseDto> getAllByTrackGuestNickname(String nickname) {
        List<Album> albums = albumRepository.findAllByTrackGuestNickname(nickname);
        return albumMapper.toAlbumPreviewResponseDtoList(albums);
    }

    @Transactional
    public List<String> getAllMusicians(UUID albumId) {
        Album album = this.getById(albumId);
        return album.getMusicianNicknames();
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

    private List<String> extractTrackGuestNicknames(List<CreateTrackDto> trackDtoList, List<String> albumMusicianNicknames) {
        Set<String> albumMusicianNicknamesSet = new HashSet<>(albumMusicianNicknames);
        return trackDtoList.stream()
                .flatMap(track -> track.getMusicianNicknames().stream())
                .filter(nickname -> !albumMusicianNicknamesSet.contains(nickname))
                .distinct()
                .toList();
    }

    private Integer combineTotalDurationSeconds(List<TrackPreviewResponseDto> trackList) {
        return trackList.stream()
                .mapToInt(TrackPreviewResponseDto::getDurationSeconds)
                .sum();
    }
}
