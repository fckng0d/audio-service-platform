package com.github.fckng0d.storageservice.grpc.server;

import com.github.fckng0d.dto.storageservice.AudioResponseDto;
import com.github.fckng0d.storageservice.domain.Audio;
import com.github.fckng0d.storageservice.mapper.internal.AudioMapper;
import com.github.fckng0d.storageservice.service.AudioService;
import com.github.fckng0d.grpc.storageservice.AudioServiceGrpc;
import com.github.fckng0d.grpc.storageservice.AudioIdRequest;
import com.github.fckng0d.grpc.storageservice.AudioResponse;
import com.github.fckng0d.grpc.storageservice.UploadAudioRequest;
import com.github.fckng0d.grpc.storageservice.AudioDataResponse;
import com.github.fckng0d.grpc.storageservice.AudioUrlRequest;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class AudioGrpcServiceImpl extends AudioServiceGrpc.AudioServiceImplBase {
    private final AudioService audioService;
    private final AudioMapper audioMapper;

    @Override
    public void uploadAudio(UploadAudioRequest request, StreamObserver<AudioResponse> responseObserver) {
        var requestDto = audioMapper.toUploadAudioRequestDto(request);
        var responseDto = audioService.uploadAudio(requestDto);

        var response = AudioResponse.newBuilder()
                .setAudioId(responseDto.getAudioId())
                .setAudioUrl(responseDto.getAudioUrl())
                .setDurationSeconds(responseDto.getDurationSeconds())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAudioById(AudioIdRequest request, StreamObserver<Empty> responseObserver) {
        long audioId = request.getAudioId();
        audioService.deleteAudioById(audioId);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAudioByUrl(AudioUrlRequest request, StreamObserver<Empty> responseObserver) {
        String audioUrl = request.getAudioUrl();
        audioService.deleteAudioByUrl(audioUrl);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void getAudioById(AudioIdRequest request, StreamObserver<AudioResponse> responseObserver) {
        Audio audio = audioService.getAudioById(request.getAudioId());
        AudioResponse response = AudioResponse.newBuilder()
                .setAudioId(audio.getId())
                .setAudioUrl(audio.getUrl())
                .setDurationSeconds(audio.getDurationSeconds())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAudioByUrl(AudioUrlRequest request, StreamObserver<AudioResponse> responseObserver) {
        Audio audio = audioService.getAudioByUrl(request.getAudioUrl());
        AudioResponse response = AudioResponse.newBuilder()
                .setAudioId(audio.getId())
                .setAudioUrl(audio.getUrl())
                .setDurationSeconds(audio.getDurationSeconds())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getAudioDataByUrl(AudioUrlRequest request, StreamObserver<AudioDataResponse> responseObserver) {
        var audioDataResponseDto = audioService.getAudioDataByUrl(request.getAudioUrl());
        var audioDataResponse = audioMapper.toAudioDataResponse(audioDataResponseDto);

        responseObserver.onNext(audioDataResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getAudioDataById(AudioIdRequest request, StreamObserver<AudioDataResponse> responseObserver) {
        var audioDataResponseDto = audioService.getAudioDataById(request.getAudioId());
        var audioDataResponse = audioMapper.toAudioDataResponse(audioDataResponseDto);

        responseObserver.onNext(audioDataResponse);
        responseObserver.onCompleted();
    }
}
