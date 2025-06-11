package com.github.fckng0d.audioservice.grpc.client;

import com.github.fckng0d.audioservice.mapper.grpc.TrackMapper;
import com.github.fckng0d.audioservice.mapper.grpc.ImageMapper;
import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.AudioDataResponseDto;
import com.github.fckng0d.dto.storageservice.AudioResponseDto;
import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.grpc.storageservice.ImageDataResponse;
import com.github.fckng0d.grpc.storageservice.ImageServiceGrpc;
import com.github.fckng0d.grpc.storageservice.AudioServiceGrpc;
import com.github.fckng0d.grpc.storageservice.UploadImageRequest;
import com.github.fckng0d.grpc.storageservice.AudioUrlRequest;
import com.github.fckng0d.grpc.storageservice.AudioDataResponse;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StorageServiceGrpcClient {

    @GrpcClient("storage-service")
    private ImageServiceGrpc.ImageServiceBlockingStub imageServiceBlockingStub;

    @GrpcClient("storage-service")
    private AudioServiceGrpc.AudioServiceBlockingStub audioServiceBlockingStub;

    private final ImageMapper imageMapper;
    private final TrackMapper trackMapper;

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

    public AudioResponseDto uploadAudio(UploadFileDto requestDto) {
        var request = trackMapper.toUploadAudioRequest(requestDto);
        var audioResponse = audioServiceBlockingStub.uploadAudio(request);

        return AudioResponseDto.builder()
                .audioUrl(audioResponse.getAudioUrl())
                .durationSeconds((short) audioResponse.getDurationSeconds())
                .build();
    }

    public AudioDataResponseDto getAudioDataByUrl(String url) {
        AudioUrlRequest request = AudioUrlRequest.newBuilder()
                .setAudioUrl(url)
                .build();

        AudioDataResponse response = audioServiceBlockingStub.getAudioDataByUrl(request);
        return trackMapper.toAudioDataResponseDto(response);
    }

    public void deleteAudioByUrl(String url) {
        AudioUrlRequest request = AudioUrlRequest.newBuilder()
                .setAudioUrl(url)
                .build();

        audioServiceBlockingStub.deleteAudioByUrl(request);
    }
}
