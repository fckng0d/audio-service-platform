package com.github.fckng0d.s3service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3client;

    @Value("${aws.s3-bucket}")
    private String bucketName;

    public String uploadFile(UploadFileRequestDto uploadFileRequestDto) throws IOException {
        String fileName = uploadFileRequestDto.getFileName();
        String folderName = uploadFileRequestDto.getFolderName();
        byte[] fileData = uploadFileRequestDto.getFileData();

        String keyName = folderName
                .concat("/")
                .concat(generateUniqueFileName(fileName));

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(fileData.length);

        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(fileData)) {
            PutObjectRequest request = new PutObjectRequest(bucketName, keyName, inputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            s3client.putObject(request);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }

        return s3client.getUrl(bucketName, keyName).toString();
    }

    private static String generateUniqueFileName(String fileName) {
        String originalFileName = fileName.substring(0, fileName.lastIndexOf("."));
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));

        String uniqueAddition = UUID.randomUUID().toString();

        return originalFileName
                .concat("_")
                .concat(uniqueAddition)
                .concat(".")
                .concat(fileExtension);
    }

    public byte[] getFile(String url) {
        S3Object object = s3client.getObject(bucketName, url);
        return this.getFileBytes(object);
    }

    private byte[] getFileBytes(S3Object object) {
        try (S3ObjectInputStream inputStream = object.getObjectContent()) {
            return IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file from S3", e);
        }
    }

    public void deleteFile(String url) {
        s3client.deleteObject(bucketName, url);
    }
}
