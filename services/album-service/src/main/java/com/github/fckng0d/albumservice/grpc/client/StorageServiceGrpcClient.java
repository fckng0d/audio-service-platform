package com.github.fckng0d.albumservice.grpc.client;

import com.github.fckng0d.albumservice.mapper.grpc.ImageMapper;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.github.fckng0d.grpc.storageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceGrpcClient {

    @GrpcClient("storage-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    private final ImageMapper imageMapper;

    public String uploadImage(UploadFileDto requestDto) {
        UploadImageRequest request = imageMapper.toUploadImageRequest(requestDto);
        return imageServiceBlockingStub.uploadImage(request).getImageUrl();
    }

    public ImageDataResponseDto getImageDataByUrl(String url) {
        com.github.fckng0d.grpc.storageservice.ImageUrlRequest request = com.github.fckng0d.grpc.storageservice.ImageUrlRequest.newBuilder()
                .setImageUrl(url)
                .build();

        ImageDataResponse response = imageServiceBlockingStub.getImageDataByUrl(request);
        return imageMapper.toImageDataResponseDto(response);
    }

    public void deleteImageByUrl(String url) {
        com.github.fckng0d.grpc.storageservice.ImageUrlRequest request = com.github.fckng0d.grpc.storageservice.ImageUrlRequest.newBuilder()
                .setImageUrl(url)
                .build();

        imageServiceBlockingStub.deleteImageByUrl(request);
    }
}
