package com.github.fckng0d.storageservice.mapper.internal;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    @Mapping(target = "imageFileData", source = "imageFileData", qualifiedByName = "byteArrayToByteString")
    ImageDataResponse toImageDataResponse(ImageDataResponseDto dto);

    @Named("byteArrayToByteString")
    static ByteString byteArrayToByteString(byte[] byteArray) {
        return byteArray == null ? null : ByteString.copyFrom(byteArray);
    }

    @Mapping(target = "fileData", source = "imageFileData", qualifiedByName = "mapByteString")
    UploadFileDto toUploadImageRequestDto(UploadImageRequest request);

    @Named("mapByteString")
    static byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }
}
