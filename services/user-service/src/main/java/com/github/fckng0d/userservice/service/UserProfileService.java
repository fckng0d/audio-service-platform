package com.github.fckng0d.userservice.service;

import com.github.fckng0d.dto.imageservice.UploadImageRequestDto;
import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.exception.profile.MusicianProfileAlreadyAssignedException;
import com.github.fckng0d.userservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.userservice.repositoty.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final ImageServiceGrpcClient imageServiceGrpcClient;

    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public UserProfile getUserProfileById(UUID id) {
        return userProfileRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    //TODO: обращаться к MusicianProfileService (после реализации) и создавать здесь
    public void createMusicianProfile(UUID profileId) {
//        UserProfile userProfile = this.getUserProfileById(profileId);
//
//        if (userProfile.getMusicianProfileId() != null) {
//            throw new MusicianProfileAlreadyAssignedException(profileId);
//        }
//
//        userService.assignRole(userProfile.getUser().getId(), "ROLE_MUSICIAN");
//        userProfile.setMusicianProfileId(musicianProfileId);
//        userProfileRepository.save(userProfile);
    }

    public void uploadProfileImage(UUID profileId, UploadImageRequestDto imageRequestDto) {
        Long imageId = imageServiceGrpcClient.uploadImage(imageRequestDto);

        UserProfile userProfile = this.getUserProfileById(profileId);
        if (userProfile.getImageId() != null) {
            imageServiceGrpcClient.deleteImageById(imageId);
        }
        userProfile.setImageId(imageId);
        userProfileRepository.save(userProfile);
    }

    public void deleteProfileImage(UUID profileId) {
        UserProfile userProfile = this.getUserProfileById(profileId);

        if (userProfile.getImageId() != null) {
            imageServiceGrpcClient.deleteImageById(userProfile.getImageId());
            userProfile.setImageId(null);
            userProfileRepository.save(userProfile);
        }
    }
}
