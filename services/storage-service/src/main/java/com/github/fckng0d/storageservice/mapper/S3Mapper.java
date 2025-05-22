package com.github.fckng0d.storageservice.mapper;

import com.github.fckng0d.dto.storageservice.UploadFileRequestDto;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import com.github.fckng0d.grpc.musicianservice.UploadFileRequest;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface S3Mapper {
    @Mapping(target = "fileData", source = "fileData", qualifiedByName = "mapByteString")
    UploadFileRequestDto toUploadFileRequestDto(UploadFileRequest request);

    @Named("mapByteString")
    default byte[] mapByteString(ByteString value) {
        return value != null ? value.toByteArray() : null;
    }
}
