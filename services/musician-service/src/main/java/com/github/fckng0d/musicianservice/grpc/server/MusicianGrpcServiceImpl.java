package com.github.fckng0d.musicianservice.grpc.server;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.grpc.musicianservice.AlbumIdsResponse;
import com.github.fckng0d.grpc.musicianservice.CreateMusicianRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianByIdRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianByNicknameRequest;
import com.github.fckng0d.grpc.musicianservice.MusicianResponse;
import com.github.fckng0d.grpc.musicianservice.MusicianServiceGrpc;
import com.github.fckng0d.grpc.musicianservice.MusicianSubscriberIdsResponse;
import com.github.fckng0d.grpc.musicianservice.UpdateBioRequest;
import com.github.fckng0d.grpc.musicianservice.UpdateNicknameRequest;
import com.github.fckng0d.grpc.albumservice.CreateAlbumRequest;
import com.github.fckng0d.grpc.albumservice.AlbumResponse;
import com.github.fckng0d.grpc.musicianservice.UploadMusicianImageRequest;
import com.github.fckng0d.musicianservice.domain.Musician;
import com.github.fckng0d.dto.musicianservice.CreateMusicianDto;
import com.github.fckng0d.musicianservice.grpc.client.ImageServiceGrpcClient;
import com.github.fckng0d.musicianservice.mapper.grpc.ImageMapper;
import com.github.fckng0d.musicianservice.mapper.internal.AlbumMapper;
import com.github.fckng0d.musicianservice.mapper.internal.MusicianMapper;
import com.github.fckng0d.musicianservice.service.MusicianService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;

@GrpcService
@RequiredArgsConstructor
public class MusicianGrpcServiceImpl extends MusicianServiceGrpc.MusicianServiceImplBase {
    private final ImageServiceGrpcClient imageServiceGrpcClient;

    private final MusicianService musicianService;
    private final MusicianMapper musicianMapper;
    private final AlbumMapper albumMapper;
    private final ImageMapper imageMapper;

    @Override
    public void createMusician(CreateMusicianRequest request, StreamObserver<MusicianResponse> responseObserver) {
        var avatarImageDto = UploadFileDto.builder()
                .fileName(request.getAvatarImage().getFileName())
                .fileData(request.getAvatarImage().getFileData().toByteArray())
                .build();

        var headerImageDto = UploadFileDto.builder()
                .fileName(request.getHeaderImage().getFileName())
                .fileData(request.getHeaderImage().getFileData().toByteArray())
                .build();

        var createMusicianDto = CreateMusicianDto.builder()
                .userId(UUID.fromString(request.getUserId()))
                .nickname(request.getNickname())
                .bio(request.getBio())
                .avatarImage(avatarImageDto)
                .headerImage(headerImageDto)
                .build();

        Musician musician = musicianService.createMusician(createMusicianDto);
        var response = musicianMapper.toMusicianResponse(musician);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteMusician(MusicianByIdRequest request, StreamObserver<Empty> responseObserver) {

    }

    @Override
    public void getMusicianByNickname(MusicianByNicknameRequest request, StreamObserver<MusicianResponse> responseObserver) {
        Musician musician = musicianService.getByNickname(request.getNickname());

        var response = musicianMapper.toMusicianResponse(musician);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getMusicianById(MusicianByIdRequest request, StreamObserver<MusicianResponse> responseObserver) {
        Musician musician = musicianService.getById(UUID.fromString(request.getMusicianId()));

        var response = musicianMapper.toMusicianResponse(musician);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateNickname(UpdateNicknameRequest request, StreamObserver<MusicianResponse> responseObserver) {
        Musician musician = musicianService.updateNickname(
                UUID.fromString(request.getMusicianId()),
                request.getNewNickname()
        );
        var response = musicianMapper.toMusicianResponse(musician);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateBio(UpdateBioRequest request, StreamObserver<MusicianResponse> responseObserver) {
        Musician musician = musicianService.updateBio(
                UUID.fromString(request.getMusicianId()),
                request.getNewBio()
        );
        var response = musicianMapper.toMusicianResponse(musician);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void uploadAvatarImage(UploadMusicianImageRequest request, StreamObserver<Empty> responseObserver) {
        var avatarImageDto = UploadFileDto.builder()
                .fileName(request.getFileName())
                .fileData(request.getImageFileData().toByteArray())
                .build();
        musicianService.uploadAvatarImage(UUID.fromString(request.getMusicianId()), avatarImageDto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void uploadHeaderImage(UploadMusicianImageRequest request, StreamObserver<Empty> responseObserver) {
        var headerImageDto = UploadFileDto.builder()
                .fileName(request.getFileName())
                .fileData(request.getImageFileData().toByteArray())
                .build();
        musicianService.uploadHeaderImage(UUID.fromString(request.getMusicianId()), headerImageDto);

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteAvatarImage(MusicianByIdRequest request, StreamObserver<Empty> responseObserver) {
        musicianService.deleteAvatarImage(UUID.fromString(request.getMusicianId()));

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void deleteHeaderImage(MusicianByIdRequest request, StreamObserver<Empty> responseObserver) {
        musicianService.deleteHeaderImage(UUID.fromString(request.getMusicianId()));

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }

    @Override
    public void createAlbum(CreateAlbumRequest request, StreamObserver<AlbumResponse> responseObserver) {
        var uploadImageDto = imageMapper.toUploadFileRequestDto(request);

        var albumRequestDto = albumMapper.toCreateAlbumDto(request);
        albumRequestDto.setMusicianIds(request.getMusicianIdsList().stream()
                .map(UUID::fromString)
                .toList());
        albumRequestDto.setCoverImage(uploadImageDto);

        var albumResponseDto = musicianService.createAlbum(albumRequestDto);
        var albumResponse = albumMapper.toAlbumResponse(albumResponseDto);

        responseObserver.onNext(albumResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getAllAlbums(MusicianByIdRequest request, StreamObserver<AlbumIdsResponse> responseObserver) {
        super.getAllAlbums(request, responseObserver);
    }

    @Override
    public void getSubscriberIds(MusicianByIdRequest request, StreamObserver<MusicianSubscriberIdsResponse> responseObserver) {
        List<UUID> subscriberIds = musicianService.getSubscribersIds(UUID.fromString(request.getMusicianId()));

        var response = MusicianSubscriberIdsResponse.newBuilder()
                .addAllSubscriberIds(subscriberIds.stream()
                        .map(UUID::toString)
                        .toList())
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
