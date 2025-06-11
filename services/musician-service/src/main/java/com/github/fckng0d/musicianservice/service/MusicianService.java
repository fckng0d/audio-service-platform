package com.github.fckng0d.musicianservice.service;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.albumservice.AlbumPreviewResponseDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import com.github.fckng0d.musicianservice.domain.Musician;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.musicianservice.grpc.client.AlbumServiceGrpcClient;
import com.github.fckng0d.musicianservice.grpc.client.StorageServiceGrpcClient;
import com.github.fckng0d.musicianservice.mapper.grpc.AlbumMapper;
import com.github.fckng0d.musicianservice.mapper.internal.MusicianMapper;
import com.github.fckng0d.musicianservice.repository.MusicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class MusicianService {
    private final StorageServiceGrpcClient storageServiceGrpcClient;
    private final AlbumServiceGrpcClient albumServiceGrpcClint;

    private final MusicianRepository musicianRepository;
    private final AlbumMapper albumMapper;
    private final MusicianMapper musicianMapper;

    @Transactional
    public Musician createMusician(CreateMusicianDto musicianDto) {
        if (this.existsByNickname(musicianDto.getNickname())) {
            throw new RuntimeException("Nickname already exists");
        }

        String avatarImageUrl;
        String headerImageUrl;

        CompletableFuture<String> avatarFuture = Optional.ofNullable(musicianDto.getAvatarImage())
                .map(img -> CompletableFuture.supplyAsync(() -> storageServiceGrpcClient.uploadImage(img)))
                .orElse(CompletableFuture.completedFuture(null));

        CompletableFuture<String> headerFuture = Optional.ofNullable(musicianDto.getHeaderImage())
                .map(img -> CompletableFuture.supplyAsync(() -> storageServiceGrpcClient.uploadImage(img)))
                .orElse(CompletableFuture.completedFuture(null));

        try {
            avatarImageUrl = avatarFuture.get();
            headerImageUrl = headerFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Image upload failed", e);
        }

        Musician musician = Musician.builder()
                .userId(musicianDto.getUserId())
                .nickname(musicianDto.getNickname())
                .bio(musicianDto.getBio())
                .isVerified(false)
                .isBlocked(false)
                .avatarImageUrl(avatarImageUrl)
                .headerImageUrl(headerImageUrl)
                .build();

        return musicianRepository.save(musician);
    }

    @Transactional
    public MusicianResponseDto getMusicianByNickname(String nickname) {
        Musician musician = this.getByNickname(nickname);

        List<AlbumPreviewResponseDto> albums;
        List<AlbumPreviewResponseDto> guestAlbums;

        CompletableFuture<List<AlbumPreviewResponseDto>> albumsFuture =
                CompletableFuture.supplyAsync(() -> albumServiceGrpcClint.getAlbumsByMusician(nickname));

        CompletableFuture<List<AlbumPreviewResponseDto>> guestAlbumsFuture =
                CompletableFuture.supplyAsync(() -> albumServiceGrpcClint.getGuestAlbumsByMusician(nickname));
        try {
            albums = albumsFuture.get();
            guestAlbums = guestAlbumsFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("", e);
        }

        return musicianMapper.toMusicianResponseDtoBuilder(musician)
                .albums(albums)
                .guestAlbums(guestAlbums)
                .build();
    }

    private Musician getById(UUID id) {
        return musicianRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException(""));
    }

    private Musician getByNickname(String nickname) {
        return musicianRepository.findByNickname(nickname)
                .orElseThrow(() -> new RuntimeException(""));
    }

    private boolean existsByNickname(String nickname) {
        return musicianRepository.existsByNickname(nickname);
    }

    public Musician updateNickname(UUID musicianId, String newNickname) {
        if (this.existsByNickname(newNickname)) {
            throw new RuntimeException("Nickname already exists");
        }

        Musician musician = this.getById(musicianId);
        musician.setNickname(newNickname);

        return musicianRepository.save(musician);
    }

    public Musician updateBio(UUID musicianId, String newBio) {
        Musician musician = this.getById(musicianId);
        musician.setBio(newBio);

        return musicianRepository.save(musician);
    }

    @Transactional
    public void uploadAvatarImage(UUID musicianId, UploadFileDto avatarImageDto) {
        Musician musician = this.getById(musicianId);

        String avatarImageUrl = storageServiceGrpcClient.uploadImage(avatarImageDto);

        musician.setAvatarImageUrl(avatarImageUrl);
        musicianRepository.save(musician);
    }

    @Transactional
    public void uploadHeaderImage(UUID musicianId, UploadFileDto headerImageDto) {
        Musician musician = this.getById(musicianId);

        String headerImageUrl = storageServiceGrpcClient.uploadImage(headerImageDto);

        musician.setHeaderImageUrl(headerImageUrl);
        musicianRepository.save(musician);
    }

    public void deleteAvatarImage(UUID musicianId) {
        Musician musician = this.getById(musicianId);

        if (musician.getAvatarImageUrl() == null) {
            throw new RuntimeException("");
        }

        storageServiceGrpcClient.deleteImageByUrl(musician.getAvatarImageUrl());
        musician.setAvatarImageUrl(null);

        musicianRepository.save(musician);
    }

    public void deleteHeaderImage(UUID musicianId) {
        Musician musician = this.getById(musicianId);

        if (musician.getHeaderImageUrl() == null) {
            throw new RuntimeException("");
        }

        storageServiceGrpcClient.deleteImageByUrl(musician.getHeaderImageUrl());
        musician.setHeaderImageUrl(null);

        musicianRepository.save(musician);
    }

    public List<UUID> getSubscribersIds(UUID musicianId) {
        return this.getById(musicianId).getSubscriberIds();
    }

    @Transactional
    public AlbumResponseDto createAlbum(CreateAlbumDto albumRequestDto) {
        var albumResponseDto = albumServiceGrpcClint.createAlbum(albumRequestDto);

        var albumId = albumResponseDto.getAlbumId();
        Set<String> allMusicianNicknames = Stream.concat(
                albumResponseDto.getMusicianNicknames().stream(),
                albumResponseDto.getTrackGuestNicknames().stream()
        ).collect(Collectors.toSet());

        List<Musician> updatedMusicians = allMusicianNicknames.stream()
                .map(this::getByNickname)
                .peek(musician -> {
                    String nickname = musician.getNickname();
                    if (albumResponseDto.getMusicianNicknames().contains(nickname)) {
                        musician.getAlbumIds().add(albumId);
                    }
                    if (albumResponseDto.getTrackGuestNicknames().contains(nickname)) {
                        musician.getGuestAlbumIds().add(albumId);
                    }
                })
                .toList();
        musicianRepository.saveAll(updatedMusicians);

        return albumResponseDto;
    }

    @Transactional
    public void deleteAlbumById(UUID albumId) {
        List<String> musicianNicknames = albumServiceGrpcClint.deleteAlbumById(albumId);

        List<Musician> musiciansToUpdate = musicianNicknames.stream()
                .map(this::getByNickname)
                .peek(musician -> musician.getAlbumIds().remove(albumId))
                .peek(musician -> musician.getGuestAlbumIds().remove(albumId))
                .toList();

        musicianRepository.saveAll(musiciansToUpdate);
    }

}
