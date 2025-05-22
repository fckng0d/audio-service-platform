package com.github.fckng0d.storageservice.mapper.grpc;

import com.github.fckng0d.dto.storageservice.UploadFileRequestDto;
import com.github.fckng0d.grpc.musicianservice.UploadFileRequest;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface S3Mapper {
    @Mapping(target = "fileData", source = "fileData", qualifiedByName = "mapByteString")
    UploadFileRequestDto toUploadFileRequestDto(UploadFileRequest request);

    @Named("mapByteString")
    static byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }

    @Mapping(target = "fileData", source = "fileData", qualifiedByName = "byteArrayToByteString")
    UploadFileRequest toUploadFileRequest(UploadFileRequestDto dto);

    @Named("byteArrayToByteString")
    static ByteString byteArrayToByteString(byte[] byteArray) {
        return byteArray == null ? null : ByteString.copyFrom(byteArray);
    }
}
