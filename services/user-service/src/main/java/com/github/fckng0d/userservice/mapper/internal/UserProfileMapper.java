package com.github.fckng0d.userservice.mapper.internal;

import com.github.fckng0d.grpc.userservice.UserProfileResponse;
import com.github.fckng0d.userservice.domain.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {

//    @Mapping(source = "id", target = "profileId", qualifiedByName = "mapUUID")
//    @Mapping(source = "imageId", target = "imageId")
//    @Mapping(source = "registrationDate", target = "registrationDate", qualifiedByName = "mapInstantToString")
//    @Mapping(source = "musicianProfileId", target = "musicianProfileId", qualifiedByName = "mapUUID")
//    UserProfileResponse toUserProfileResponse(UserProfile userProfile);
//
//    @Named("mapUUID")
//    default String mapUUID(UUID id) {
//        return id.toString();
//    }
//
//    @Named("mapInstantToString")
//    default String mapInstantToString(Instant instant) {
//        return instant != null ? instant.toString() : null;
//    }
}
