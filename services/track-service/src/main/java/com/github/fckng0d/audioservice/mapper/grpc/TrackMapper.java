package com.github.fckng0d.audioservice.mapper.grpc;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.AudioDataResponseDto;
import com.google.protobuf.ByteString;
import com.github.fckng0d.grpc.storageservice.UploadAudioRequest;
import com.github.fckng0d.grpc.storageservice.AudioDataResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface TrackMapper {
    @Mapping(target = "audioFileData", source = "fileData", qualifiedByName = "byteArrayToByteString")
    UploadAudioRequest toUploadAudioRequest(UploadFileDto dto);

    default UploadFileDto toUploadFileRequestDto(com.github.fckng0d.grpc.albumservice.CreateAlbumRequest request) {
        return UploadFileDto.builder()
                .fileName(request.getCoverImage().getFileName())
                .fileData(request.getCoverImage().getFileData().toByteArray())
                .build();
    }

    @Mapping(target = "audioFileData", source = "audioFileData", qualifiedByName = "mapByteString")
    AudioDataResponseDto toAudioDataResponseDto(AudioDataResponse response) ;

    @Named("byteArrayToByteString")
    static ByteString byteArrayToByteString(byte[] byteArray) {
        return byteArray != null ? ByteString.copyFrom(byteArray) : ByteString.EMPTY;
    }

    @Named("mapByteString")
    static byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }
}