package com.github.fckng0d.musicianservice.service;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.musicianservice.domain.Musician;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.musicianservice.grpc.client.AlbumServiceGrpcClient;
import com.github.fckng0d.musicianservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.musicianservice.mapper.internal.AlbumMapper;
import com.github.fckng0d.musicianservice.repository.MusicianRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MusicianService {
    private final ImageServiceGrpcClient imageServiceGrpcClient;
    private final AlbumServiceGrpcClient albumServiceGrpcClint;

    private final MusicianRepository musicianRepository;
    private final AlbumMapper albumMapper;

    @Transactional
    public Musician createMusician(CreateMusicianDto musicianDto) {
        if (this.existsByNickname(musicianDto.getNickname())) {
            throw new RuntimeException("Nickname already exists");
        }

        String avatarImageUrl = Optional.ofNullable(musicianDto.getAvatarImage())
                .map(imageServiceGrpcClient::uploadImage)
                .orElse(null);

        String headerImageUrl = Optional.ofNullable(musicianDto.getAvatarImage())
                .map(imageServiceGrpcClient::uploadImage)
                .orElse(null);

        Musician musician = Musician.builder()
                .userId(musicianDto.getUserId())
                .nickname(musicianDto.getNickname())
                .bio(musicianDto.getBio())
                .avatarImageUrl(avatarImageUrl)
                .headerImageUrl(headerImageUrl)
                .build();

        return musicianRepository.save(musician);
    }

    public Musician getById(UUID id) {
        return musicianRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException(""));
    }

    public Musician getByNickname(String nickname) {
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

        String avatarImageUrl = imageServiceGrpcClient.uploadImage(avatarImageDto);

        musician.setAvatarImageUrl(avatarImageUrl);
        musicianRepository.save(musician);
    }

    @Transactional
    public void uploadHeaderImage(UUID musicianId, UploadFileDto headerImageDto) {
        Musician musician = this.getById(musicianId);

        String headerImageUrl = imageServiceGrpcClient.uploadImage(headerImageDto);

        musician.setHeaderImageUrl(headerImageUrl);
        musicianRepository.save(musician);
    }

    public void deleteAvatarImage(UUID musicianId) {
        Musician musician = this.getById(musicianId);

        if (musician.getAvatarImageUrl() == null) {
            throw new RuntimeException("");
        }

        imageServiceGrpcClient.deleteImageByUrl(musician.getAvatarImageUrl());
        musician.setAvatarImageUrl(null);

        musicianRepository.save(musician);
    }

    public void deleteHeaderImage(UUID musicianId) {
        Musician musician = this.getById(musicianId);

        if (musician.getHeaderImageUrl() == null) {
            throw new RuntimeException("");
        }

        imageServiceGrpcClient.deleteImageByUrl(musician.getHeaderImageUrl());
        musician.setHeaderImageUrl(null);

        musicianRepository.save(musician);
    }

    public List<UUID> getSubscribersIds(UUID musicianId) {
        return this.getById(musicianId).getSubscriberIds();
    }

    public AlbumResponseDto createAlbum(CreateAlbumDto albumRequestDto) {
        return albumServiceGrpcClint.createAlbum(albumRequestDto);
    }

    @Transactional
    public void deleteAlbumById(UUID albumId) {
        List<UUID> musicianIds = albumServiceGrpcClint.deleteAlbumById(albumId);

        List<Musician> musiciansToUpdate = musicianIds.stream()
                .map(this::getById)
                .peek(musician -> musician.getAlbumIds().remove(albumId))
                .toList();

        musicianRepository.saveAll(musiciansToUpdate);
    }

}
