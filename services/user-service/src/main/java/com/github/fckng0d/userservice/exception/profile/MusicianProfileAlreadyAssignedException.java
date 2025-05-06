package com.github.fckng0d.userservice.exception.profile;

import java.util.UUID;

public class MusicianProfileAlreadyAssignedException extends RuntimeException {
    public MusicianProfileAlreadyAssignedException(UUID profileId) {
        super("Musician profile is already assigned to this user-profile: " + profileId);
    }
}
