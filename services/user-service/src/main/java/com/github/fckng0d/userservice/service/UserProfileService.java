package com.github.fckng0d.userservice.service;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.exception.profile.MusicianProfileAlreadyAssignedException;
import com.github.fckng0d.userservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.userservice.grpc.client.MusicianServiceGrpcClient;
import com.github.fckng0d.userservice.repositoty.UserProfileRepository;
import com.github.fckng0d.grpc.musicianservice.CreateMusicianRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final ImageServiceGrpcClient imageServiceGrpcClient;
    private final MusicianServiceGrpcClient musicianServiceGrpcClient;

    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public UserProfile getUserProfileById(UUID id) {
        return userProfileRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    //TODO: обращаться к MusicianProfileService (после реализации) и создавать здесь
    @Transactional
    public MusicianResponseDto createMusicianProfile(CreateMusicianRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        User user = userService.getUserById(userId);
        UserProfile userProfile = this.getUserProfileById(user.getProfile().getId());

        if (userProfile.getMusicianProfileId() != null) {
            throw new MusicianProfileAlreadyAssignedException(userId);
        }

        var musicianResponseDto = musicianServiceGrpcClient.createMusician(request);

        userService.assignRole(user.getId(), "ROLE_MUSICIAN");
        userProfile.setMusicianProfileId(musicianResponseDto.getMusicianId());
        userProfileRepository.save(userProfile);

        return musicianResponseDto;
    }

    public void uploadProfileImage(UUID profileId, UploadFileDto imageRequestDto) {
        String newImageUrl = imageServiceGrpcClient.uploadImage(imageRequestDto);

        UserProfile userProfile = this.getUserProfileById(profileId);
        String profileImageUrl = userProfile.getImageUrl();
        if (userProfile.getImageUrl() != null) {
            imageServiceGrpcClient.deleteImageByUrl(profileImageUrl);
        }
        userProfile.setImageUrl(newImageUrl);
        userProfileRepository.save(userProfile);
    }

    public void deleteProfileImage(UUID profileId) {
        UserProfile userProfile = this.getUserProfileById(profileId);

        if (userProfile.getImageUrl() != null) {
            imageServiceGrpcClient.deleteImageByUrl(userProfile.getImageUrl());
            userProfile.setImageUrl(null);
            userProfileRepository.save(userProfile);
        }
    }
}
