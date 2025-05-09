package com.github.fckng0d.userservice.grpc.client;

import com.github.fckng0d.dto.imageservice.ImageDataResponseDto;
import com.github.fckng0d.dto.imageservice.UploadImageRequestDto;
import com.github.fckng0d.userservice.mapper.grpc.ImageMapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.imageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.imageservice.ImageIdRequest;
import com.github.fckng0d.grpc.imageservice.UploadImageRequest;
import com.github.fckng0d.grpc.imageservice.ImageDataResponse;

@Service
@RequiredArgsConstructor
public class ImageServiceGrpcClient {

    @GrpcClient("image-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    private final ImageMapper imageMapper;

    public String getImageUrlById(Long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();
        return imageServiceBlockingStub.getImageUrlById(request).getImageUrl();
    }

    public Long uploadImage(UploadImageRequestDto requestDto) {
        UploadImageRequest request = imageMapper.toUploadImageRequest(requestDto);
        return imageServiceBlockingStub.uploadImage(request).getImageId();
    }

    public ImageDataResponseDto getImageDataById(Long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();

        ImageDataResponse response = imageServiceBlockingStub.getImageDataById(request);
        return imageMapper.toImageDataResponseDto(response);
    }

    public void deleteImageById(Long imageId) {
        ImageIdRequest request = ImageIdRequest.newBuilder()
                .setImageId(imageId)
                .build();

        imageServiceBlockingStub.deleteImageById(request);
    }
}
