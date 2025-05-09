package com.github.fckng0d.imageservice.grpc.server;

import com.github.fckng0d.grpc.imageservice.ImageDataResponse;
import com.github.fckng0d.grpc.imageservice.ImageIdRequest;
import com.github.fckng0d.grpc.imageservice.ImageIdResponse;
import com.github.fckng0d.grpc.imageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.imageservice.UploadImageRequest;
import com.github.fckng0d.grpc.imageservice.ImageUrlResponse;
import com.github.fckng0d.imageservice.mapper.internal.ImageMapper;
import com.github.fckng0d.imageservice.service.ImageService;
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
    public void getImageUrlById(ImageIdRequest request, StreamObserver<ImageUrlResponse> responseObserver) {
        Long imageId = request.getImageId();
        String imageUrl = imageService.getImageUrlById(imageId);
        ImageUrlResponse response = ImageUrlResponse.newBuilder()
                .setImageUrl(imageUrl)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void uploadImage(UploadImageRequest request, StreamObserver<ImageIdResponse> responseObserver) {
        var dto = imageMapper.toUploadImageRequestDto(request);
        Long imageId = imageService.uploadImage(dto);

        ImageIdResponse response = ImageIdResponse.newBuilder()
                .setImageId(imageId)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getImageDataById(ImageIdRequest request, StreamObserver<ImageDataResponse> responseObserver) {
        Long imageId = request.getImageId();
        var dto = imageService.getImageDataById(imageId);
        ImageDataResponse response = imageMapper.toImageDataResponse(dto);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteImageById(ImageIdRequest request, StreamObserver<Empty> responseObserver) {
        Long imageId = request.getImageId();
        imageService.deleteImageById(imageId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
