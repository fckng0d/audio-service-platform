package com.github.fckng0d.imageservice.mapper.internal;

import com.github.fckng0d.dto.imageservice.ImageDataResponseDto;
import com.github.fckng0d.dto.imageservice.UploadImageRequestDto;
import com.github.fckng0d.grpc.imageservice.ImageDataResponse;
import com.github.fckng0d.grpc.imageservice.UploadImageRequest;
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

    @Mapping(target = "imageFileData", source = "imageFileData", qualifiedByName = "mapByteString")
    UploadImageRequestDto toUploadImageRequestDto(UploadImageRequest request);

    @Named("mapByteString")
    static byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }
}
