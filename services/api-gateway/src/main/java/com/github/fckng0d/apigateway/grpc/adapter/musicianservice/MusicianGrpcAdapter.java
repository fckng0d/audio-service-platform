package com.github.fckng0d.apigateway.grpc.adapter.musicianservice;

import com.github.fckng0d.apigateway.mapper.AlbumMapper;
import com.github.fckng0d.apigateway.mapper.MusicianMapper;
import com.github.fckng0d.apigateway.mapper.TrackMapper;
import com.github.fckng0d.dto.albumservice.AlbumResponseDto;
import com.github.fckng0d.dto.albumservice.CreateAlbumDto;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.dto.musicianservice.MusicianResponseDto;
import com.github.fckng0d.grpc.musicianservice.MusicianByNicknameRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianServiceGrpc;
import com.github.fckng0d.grpc.musicianservice.CreateMusicianRequest;
import com.github.fckng0d.grpc.musicianservice.UploadFileRequest;
import com.github.fckng0d.grpc.trackservice.CreateTrackRequest;
import com.google.protobuf.ByteString;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class MusicianGrpcAdapter {

    @GrpcClient("musician-service")
    private MusicianServiceGrpc.MusicianServiceBlockingStub musicianServiceBlockingStub;

    private final MusicianMapper musicianMapper;
    private final AlbumMapper albumMapper;
    private final TrackMapper trackMapper;

    public Mono<MusicianResponseDto> createMusician(CreateMusicianDto createMusicianDto) {
        return Mono.fromCallable(() -> {
            var avatarImageDto = createMusicianDto.getAvatarImage();
            var uploadAvatarImageRequest = UploadFileRequest.newBuilder()
                    .setFileName(avatarImageDto.getFileName())
                    .setFileData(ByteString.copyFrom(avatarImageDto.getFileData()))
                    .build();

            var headerImageDto = createMusicianDto.getHeaderImage();
            var uploadHeaderImageRequest = UploadFileRequest.newBuilder()
                    .setFileName(headerImageDto.getFileName())
                    .setFileData(ByteString.copyFrom(headerImageDto.getFileData()))
                    .build();

            var request = CreateMusicianRequest.newBuilder()
                    .setUserId(createMusicianDto.getUserId().toString())
                    .setNickname(createMusicianDto.getNickname())
                    .setBio(createMusicianDto.getBio())
                    .setAvatarImage(uploadAvatarImageRequest)
                    .setHeaderImage(uploadHeaderImageRequest)
                    .build();
            var response = musicianServiceBlockingStub.createMusician(request);

            return musicianMapper.toMusicianResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<MusicianResponseDto> getMusicianByNickname(String nickname) {
        return Mono.fromCallable(() -> {
            var request = MusicianByNicknameRequest.newBuilder()
                    .setNickname(nickname)
                    .build();
            var response = musicianServiceBlockingStub.getMusicianByNickname(request);

            return musicianMapper.toMusicianResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<AlbumResponseDto> createAlbum(CreateAlbumDto createAlbumDto) {
        return Mono.fromCallable(() -> {
            var uploadCoverImageRequest = com.github.fckng0d.grpc.albumservice.UploadFileRequest.newBuilder()
                    .setFileName(createAlbumDto.getCoverImage().getFileName())
                    .setFileData(ByteString.copyFrom(createAlbumDto.getCoverImage().getFileData()))
                    .build();

            var request = CreateAlbumRequest.newBuilder()
                    .setName(createAlbumDto.getName())
                    .addAllMusicianNicknames(createAlbumDto.getMusicianNicknames())
                    .setCoverImage(uploadCoverImageRequest)
                    .addAllTracks(createAlbumDto.getTracks().stream()
                            .map(trackMapper::toTrackRequest)
                            .toList())
                    .build();

            var response = musicianServiceBlockingStub.createAlbum(request);

            return albumMapper.toAlbumResponseDto(response);
        }).subscribeOn(Schedulers.boundedElastic());
    }

}
