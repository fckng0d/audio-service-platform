package com.github.fckng0d.userservice.grpc.client;

import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.dto.UploadFileDto;
//import com.github.fckng0d.userservice.mapper.grpc.ImageMapper2;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.storageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.storageservice.ImageIdRequest;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.github.fckng0d.grpc.storageservice.ImageUrlRequest;

@Service
@RequiredArgsConstructor
public class StorageServiceGrpcClient {

    @GrpcClient("storage-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

//    private final ImageMapper2 imageMapper2;

    public String getImageById(long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();
        return imageServiceBlockingStub.getImageById(request).getImageUrl();
    }

    public String uploadImage(UploadFileDto requestDto) {
        UploadImageRequest request = UploadImageRequest.newBuilder()
                .setFileName(requestDto.getFileName())
                .setImageFileData(ByteString.copyFrom(requestDto.getFileData()))
                .build();
        return imageServiceBlockingStub.uploadImage(request).getImageUrl();
    }

    public ImageDataResponseDto getImageDataById(long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();

        ImageDataResponse response = imageServiceBlockingStub.getImageDataById(request);
        return ImageDataResponseDto.builder()
                .originalFileName(response.getOriginalFileName())
                .fileExtension(response.getFileExtension())
                .imageFileData(response.getImageFileData().toByteArray())
                .build();
    }

    public void deleteImageById(long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();

        imageServiceBlockingStub.deleteImageById(request);
    }

    public void deleteImageByUrl(String imageUrl) {
        ImageUrlRequest request = ImageUrlRequest.newBuilder()
                .setImageUrl(imageUrl)
                .build();

        imageServiceBlockingStub.deleteImageByUrl(request);
    }
}
