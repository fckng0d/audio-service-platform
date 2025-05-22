package com.github.fckng0d.userservice.mapper.grpc;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(target = "imageFileData", source = "imageFileData", qualifiedByName = "byteArrayToByteString")
    UploadImageRequest toUploadImageRequest(UploadFileDto dto);

    @Mapping(target = "imageFileData", source = "imageFileData", qualifiedByName = "mapByteString")
    ImageDataResponseDto toImageDataResponseDto(ImageDataResponse response);

    @Named("byteArrayToByteString")
    static ByteString byteArrayToByteString(byte[] byteArray) {
        return byteArray != null ? ByteString.copyFrom(byteArray) : ByteString.EMPTY;
    }

    @Named("mapByteString")
    static byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }
}
