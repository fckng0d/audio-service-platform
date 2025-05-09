package com.github.fckng0d.imageservice.grpc.client;

import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import com.github.fckng0d.imageservice.mapper.grpc.S3Mapper;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import com.github.fckng0d.grpc.s3service.S3ServiceGrpc;
import com.github.fckng0d.grpc.s3service.UploadFileRequest;
import com.github.fckng0d.grpc.s3service.FileUrlResponse;
import com.github.fckng0d.grpc.s3service.FileUrlRequest;
import com.github.fckng0d.grpc.s3service.FileDataResponse;

@Service
@RequiredArgsConstructor
public class S3ServiceGrpcClient {

    @GrpcClient("s3-service")
    private S3ServiceGrpc.S3ServiceBlockingStub s3ServiceBlockingStub;

    private final S3Mapper s3Mapper;

    public String uploadFile(UploadFileRequestDto uploadFileRequestDto) {
        UploadFileRequest request = s3Mapper.toUploadFileRequest(uploadFileRequestDto);
        FileUrlResponse response = s3ServiceBlockingStub.uploadFile(request);

        return response.getFileUrl();
    }

    public byte[] getFile(String fileUrl) {
        FileUrlRequest request = FileUrlRequest.newBuilder().setFileUrl(fileUrl).build();

        FileDataResponse fileData = s3ServiceBlockingStub.getFile(request);
        return fileData.toByteArray();
    }

    public void deleteFile(String fileUrl) {
        FileUrlRequest request = FileUrlRequest.newBuilder().setFileUrl(fileUrl).build();
        s3ServiceBlockingStub.deleteFile(request);
    }
}
