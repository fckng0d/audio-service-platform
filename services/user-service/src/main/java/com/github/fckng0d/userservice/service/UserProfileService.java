package com.github.fckng0d.userservice.service;

import com.github.fckng0d.userservice.domain.UserProfile;
import com.github.fckng0d.userservice.exception.profile.MusicianProfileAlreadyAssignedException;
import com.github.fckng0d.userservice.repositoty.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {
    private final UserProfileRepository userProfileRepository;
    private final UserService userService;

    public UserProfile getUserProfileById(UUID id) {
        return userProfileRepository.findById(id)
                .orElseThrow(RuntimeException::new);
    }

    public void addMusicianProfileId(UUID profileId, UUID musicianProfileId) {
        UserProfile userProfile = this.getUserProfileById(profileId);

        if (userProfile.getMusicianProfileId() != null) {
            throw new MusicianProfileAlreadyAssignedException(profileId);
        }

        userService.assignRole(userProfile.getUser().getId(), "ROLE_MUSICIAN");
        userProfile.setMusicianProfileId(musicianProfileId);
        userProfileRepository.save(userProfile);
    }

    public void setImageId(UUID profileId, Long imageId) {
        UserProfile userProfile = this.getUserProfileById(profileId);
        userProfile.setImageId(imageId);
        userProfileRepository.save(userProfile);
    }
}
