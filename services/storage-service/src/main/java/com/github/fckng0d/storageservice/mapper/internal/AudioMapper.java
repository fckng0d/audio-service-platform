package com.github.fckng0d.storageservice.mapper.internal;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.AudioDataResponseDto;
import com.github.fckng0d.grpc.storageservice.AudioDataResponse;
import com.github.fckng0d.grpc.storageservice.UploadAudioRequest;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AudioMapper {
    @Mapping(target = "audioFileData", source = "audioFileData", qualifiedByName = "byteArrayToByteString")
    AudioDataResponse toAudioDataResponse(AudioDataResponseDto dto);

    @Named("byteArrayToByteString")
    static ByteString byteArrayToByteString(byte[] byteArray) {
        return byteArray == null ? null : ByteString.copyFrom(byteArray);
    }

    @Mapping(target = "fileData", source = "audioFileData", qualifiedByName = "mapByteString")
    UploadFileDto toUploadAudioRequestDto(UploadAudioRequest request);

    @Named("mapByteString")
    static byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }
}
