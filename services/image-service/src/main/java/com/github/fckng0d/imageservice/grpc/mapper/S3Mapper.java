package com.github.fckng0d.imageservice.grpc.mapper;

import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import com.github.fckng0d.grpc.s3service.UploadFileRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface S3Mapper {
    @Mapping(target = "fileName", source = "file_name")
    @Mapping(target = "folderName", source = "folder_name")
    @Mapping(target = "fileData", source = "file_data")
    UploadFileRequestDto toUploadFileRequestDto(UploadFileRequest request);


    @Mapping(target = "file_name", source = "fileName")
    @Mapping(target = "folder_name", source = "folderName")
    @Mapping(target = "file_data", source = "fileData")
    UploadFileRequest toUploadFileRequest(UploadFileRequestDto dto);
}
