package com.github.fckng0d.s3service.grpc.server;


import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import com.github.fckng0d.s3service.service.S3Service;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import lombok.RequiredArgsConstructor;
import com.github.fckng0d.s3service.mapper.S3Mapper;
import net.devh.boot.grpc.server.service.GrpcService;
import io.grpc.stub.StreamObserver;
import com.github.fckng0d.grpc.s3service.S3ServiceGrpc;
import com.github.fckng0d.grpc.s3service.UploadFileRequest;
import com.github.fckng0d.grpc.s3service.FileUrlResponse;
import com.github.fckng0d.grpc.s3service.FileUrlRequest;
import com.github.fckng0d.grpc.s3service.FileDataResponse;
import org.apache.tomcat.util.http.fileupload.FileUploadException;

import java.io.IOException;

@GrpcService
@RequiredArgsConstructor
public class S3GrpcServiceImpl extends S3ServiceGrpc.S3ServiceImplBase {
    private final S3Service s3Service;
    private final S3Mapper s3Mapper;

    @Override
    public void uploadFile(UploadFileRequest request, StreamObserver<FileUrlResponse> responseObserver) {
        if (request == null || request.getFileData().isEmpty() || request.getFileName().isEmpty()) {
            responseObserver.onError(new IllegalArgumentException("Invalid file data or file name"));
            return;
        }

        UploadFileRequestDto uploadFileRequestDto = s3Mapper.toUploadFileRequestDto(request);

        try {
            String fileUrl = s3Service.uploadFile(uploadFileRequestDto);

            FileUrlResponse response = FileUrlResponse.newBuilder()
                    .setFileUrl(fileUrl)
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (IOException e) {
            responseObserver.onError(new FileUploadException("File upload failed due to IO error", e));
        } catch (Exception e) {
            responseObserver.onError(new RuntimeException("Unexpected error during file upload", e));
        }
    }

    @Override
    public void getFile(FileUrlRequest request, StreamObserver<FileDataResponse> responseObserver) {
        if (request == null || request.getFileUrl().isBlank()) {
            responseObserver.onError(new IllegalArgumentException("Invalid file url"));
            return;
        }

        var fileData = s3Service.getFile(request.getFileUrl());
        FileDataResponse fileDataResponse = FileDataResponse.newBuilder()
                .setFileData(ByteString.copyFrom(fileData))
                .build();

        responseObserver.onNext(fileDataResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void deleteFile(FileUrlRequest request, StreamObserver<Empty> responseObserver) {
        if (request == null || request.getFileUrl().isBlank()) {
            responseObserver.onError(new IllegalArgumentException("Invalid file url"));
            return;
        }

        s3Service.deleteFile(request.getFileUrl());

        responseObserver.onNext(Empty.getDefaultInstance());
        responseObserver.onCompleted();
    }
}
