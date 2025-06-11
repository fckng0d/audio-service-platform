//package com.github.fckng0d.userservice.mapper.grpc;
//
//import com.github.fckng0d.dto.UploadFileDto;
//import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
//import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
//import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
//import com.google.protobuf.ByteString;
//import org.mapstruct.Mapper;
//
//@Mapper(componentModel = "spring")
//public interface ImageMapper2 {
//    default UploadImageRequest toUploadImageRequest(UploadFileDto dto) {
//        return UploadImageRequest.newBuilder()
//                .setFileName(dto.getFileName())
//                .setImageFileData(ByteString.copyFrom(dto.getFileData()))
//                .build();
//    }
//
//    default ImageDataResponseDto toImageDataResponseDto(ImageDataResponse response) {
//        return ImageDataResponseDto.builder()
//                .originalFileName(response.getOriginalFileName())
//                .fileExtension(response.getFileExtension())
//                .imageFileData(response.getImageFileData().toByteArray())
//                .build();
//    }
//}
