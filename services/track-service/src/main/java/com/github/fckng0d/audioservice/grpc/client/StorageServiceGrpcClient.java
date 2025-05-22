package com.github.fckng0d.audioservice.grpc.client;

import com.github.fckng0d.audioservice.mapper.grpc.AudioMapper;
import com.github.fckng0d.audioservice.mapper.grpc.ImageMapper;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.AudioDataResponseDto;
import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.github.fckng0d.grpc.storageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.storageservice.AudioServiceGrpc;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import com.github.fckng0d.grpc.storageservice.UploadAudioRequest;
import com.github.fckng0d.grpc.storageservice.AudioUrlRequest;
import com.github.fckng0d.grpc.storageservice.AudioDataResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceGrpcClient {

    @GrpcClient("image-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    @GrpcClient("image-service")
    private AudioServiceGrpc.AudioServiceBlockingStub audioServiceBlockingStub;

    private final ImageMapper imageMapper;
    private final AudioMapper audioMapper;

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

    public String uploadAudio(UploadFileDto requestDto) {
        UploadAudioRequest request = audioMapper.toUploadAudioRequest(requestDto);
        return audioServiceBlockingStub.uploadAudio(request).getAudioUrl();
    }

    public AudioDataResponseDto getAudioDataByUrl(String url) {
        AudioUrlRequest request = AudioUrlRequest.newBuilder()
                .setAudioUrl(url)
                .build();

        AudioDataResponse response = audioServiceBlockingStub.getAudioDataByUrl(request);
        return audioMapper.toAudioDataResponseDto(response);
    }

    public void deleteAudioByUrl(String url) {
        AudioUrlRequest request = AudioUrlRequest.newBuilder()
                .setAudioUrl(url)
                .build();

        audioServiceBlockingStub.deleteAudioByUrl(request);
    }
}
