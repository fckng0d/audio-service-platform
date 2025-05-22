package com.github.fckng0d.userservice.grpc.client;

import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.userservice.mapper.grpc.ImageMapper;
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
public class ImageServiceGrpcClient {

    @GrpcClient("image-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    private final ImageMapper imageMapper;

    public String getImageById(long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();
        return imageServiceBlockingStub.getImageById(request).getImageUrl();
    }

    public String uploadImage(UploadFileDto requestDto) {
        UploadImageRequest request = imageMapper.toUploadImageRequest(requestDto);
        return imageServiceBlockingStub.uploadImage(request).getImageUrl();
    }

    public ImageDataResponseDto getImageDataById(long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();

        ImageDataResponse response = imageServiceBlockingStub.getImageDataById(request);
        return imageMapper.toImageDataResponseDto(response);
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
