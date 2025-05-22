package com.github.fckng0d.storageservice.grpc.server;

import com.github.fckng0d.dto.storageservice.ImageResponseDto;
import com.github.fckng0d.storageservice.domain.Image;
import com.github.fckng0d.storageservice.mapper.internal.ImageMapper;
import com.github.fckng0d.storageservice.service.ImageService;
import com.github.fckng0d.grpc.storageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.storageservice.ImageIdRequest;
import com.github.fckng0d.grpc.storageservice.ImageResponse;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.github.fckng0d.grpc.storageservice.ImageUrlRequest;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class ImageGrpcServiceImpl extends ImageServiceGrpc.ImageServiceImplBase {
    private final ImageService imageService;
    private final ImageMapper imageMapper;

    @Override
    public void uploadImage(UploadImageRequest request, StreamObserver<ImageResponse> responseObserver) {
        var dto = imageMapper.toUploadImageRequestDto(request);
        ImageResponseDto responseDto = imageService.uploadImage(dto);

        ImageResponse response = ImageResponse.newBuilder()
                .setImageId(responseDto.getImageId())
                .setImageUrl(responseDto.getImageUrl())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteImageById(ImageIdRequest request, StreamObserver<Empty> responseObserver) {
        long imageId = request.getImageId();
        imageService.deleteImageById(imageId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteImageByUrl(ImageUrlRequest request, StreamObserver<Empty> responseObserver) {
        String imageUrl = request.getImageUrl();
        imageService.deleteImageByUrl(imageUrl);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getImageById(ImageIdRequest request, StreamObserver<ImageResponse> responseObserver) {
        Image image = imageService.getImageById(request.getImageId());
        ImageResponse response = ImageResponse.newBuilder()
                .setImageId(image.getId())
                .setImageUrl(image.getUrl())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getImageByUrl(ImageUrlRequest request, StreamObserver<ImageResponse> responseObserver) {
        Image image = imageService.getImageByUrl(request.getImageUrl());
        ImageResponse response = ImageResponse.newBuilder()
                .setImageId(image.getId())
                .setImageUrl(image.getUrl())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getImageDataByUrl(ImageUrlRequest request, StreamObserver<ImageDataResponse> responseObserver) {
        var imageDataResponseDto = imageService.getImageDataByUrl(request.getImageUrl());
        var imageDataResponse = imageMapper.toImageDataResponse(imageDataResponseDto);

        responseObserver.onNext(imageDataResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getImageDataById(ImageIdRequest request, StreamObserver<ImageDataResponse> responseObserver) {
        var imageDataResponseDto = imageService.getImageDataById(request.getImageId());
        var imageDataResponse = imageMapper.toImageDataResponse(imageDataResponseDto);

        responseObserver.onNext(imageDataResponse);
        responseObserver.onCompleted();
    }
}
