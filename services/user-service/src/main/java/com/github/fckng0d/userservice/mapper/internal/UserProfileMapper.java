package com.github.fckng0d.userservice.mapper.internal;

import com.github.fckng0d.grpc.userservice.UserProfileResponse;
import com.github.fckng0d.userservice.domain.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

    @Mapping(source = "profileId", target = "profile_id", qualifiedByName = "mapUUID")
    @Mapping(source = "imageId", target = "image_id")
    @Mapping(source = "musicianProfileId", target = "musician_profile_id", qualifiedByName = "mapUUID")
    UserProfileResponse toUserProfileResponse(UserProfile userProfile);

    @Named("mapUUID")
    default String mapUUID(UUID id) {
        return id.toString();
    }
}
