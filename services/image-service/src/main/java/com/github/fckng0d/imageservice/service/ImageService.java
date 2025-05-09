package com.github.fckng0d.imageservice.service;

import com.github.fckng0d.dto.imageservice.ImageDataResponseDto;
import com.github.fckng0d.dto.imageservice.UploadImageRequestDto;
import com.github.fckng0d.dto.s3service.UploadFileRequestDto;
import com.github.fckng0d.imageservice.exception.ImageNotFoundException;
import com.github.fckng0d.imageservice.domain.Image;
import com.github.fckng0d.imageservice.grpc.client.S3ServiceGrpcClient;
import com.github.fckng0d.imageservice.repository.ImageRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final S3ServiceGrpcClient s3ServiceGrpcClient;

    private final ImageRepository imageRepository;
    private static final String IMAGE_FOLDER_NAME = "images";

    @Transactional(readOnly = true)
    protected Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @Transactional(readOnly = true)
    protected Image getImageByUrl(String url) {
        return imageRepository.findByUrl(url)
                .orElseThrow(() -> new ImageNotFoundException(url));
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public String getImageUrlById(Long id) {
        Optional<Image> image = imageRepository.findById(id);
        return image.orElseThrow(() -> new ImageNotFoundException(id)).getUrl();
    }

    @Transactional(readOnly = true)
    public ImageDataResponseDto getImageDataById(Long id) {
        Image image = this.getImageById(id);
        var imageFileData = s3ServiceGrpcClient.getFile(image.getUrl());

        return ImageDataResponseDto.builder()
                .originalFileName(image.getOriginalFileName())
                .fileExtension(image.getFileExtension())
                .imageFileData(imageFileData)
                .build();
    }

    @Transactional()
    public void deleteImageById(Long id) {
        Image image = this.getImageById(id);
        s3ServiceGrpcClient.deleteFile(image.getUrl());
        imageRepository.delete(image);
    }
}
