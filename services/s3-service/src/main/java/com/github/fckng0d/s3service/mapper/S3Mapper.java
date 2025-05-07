package com.github.fckng0d.s3service.mapper;

import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import com.google.protobuf.ByteString;
import org.mapstruct.Mapper;
import com.github.fckng0d.grpc.s3service.UploadFileRequest;
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
