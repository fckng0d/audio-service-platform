package com.github.fckng0d.imageservice.service;

import com.github.fckng0d.dto.imageservice.UploadImageRequestDto;
import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import com.github.fckng0d.imageservice.domain.Image;
import com.github.fckng0d.imageservice.grpc.client.S3ServiceGrpcClient;
import com.github.fckng0d.imageservice.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3ServiceGrpcClient s3ServiceGrpcClient;

    private final ImageRepository imageRepository;
    private static final String IMAGE_FOLDER_NAME = "images";

    public Long uploadImage(UploadImageRequestDto imageRequestDto) {
        String fullFileName = imageRequestDto.getFileName();
        byte[] imageFileData = imageRequestDto.getImageFileData();

        UploadFileRequestDto requestDto = UploadFileRequestDto.builder()
                .folderName(IMAGE_FOLDER_NAME)
                .fileName(fullFileName)
                .fileData(imageFileData)
                .build();

        String imageUrl = s3ServiceGrpcClient.uploadFile(requestDto);

        String originFileName = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
        String fileExtension = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);

        Image image = Image.builder()
                .url(imageUrl)
                .originalFileName(originFileName)
                .fileExtension(fileExtension)
                .fileSize((long) imageFileData.length)
                .build();

        Image savedImage = imageRepository.save(image);

        return savedImage.getId();
    }

}
