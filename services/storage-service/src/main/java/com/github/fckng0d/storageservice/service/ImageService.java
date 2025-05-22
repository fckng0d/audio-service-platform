package com.github.fckng0d.storageservice.service;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.ImageDataResponseDto;
import com.github.fckng0d.dto.storageservice.ImageResponseDto;
import com.github.fckng0d.dto.storageservice.UploadFileRequestDto;
import com.github.fckng0d.storageservice.domain.Image;
import com.github.fckng0d.storageservice.exception.ImageNotFoundException;
import com.github.fckng0d.storageservice.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private static final String IMAGE_FOLDER_NAME = "images";
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public Image getImageById(long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ImageNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Image getImageByUrl(String url) {
        return imageRepository.findByUrl(url)
                .orElseThrow(() -> new ImageNotFoundException(url));
    }

    @Transactional
    public ImageResponseDto uploadImage(UploadFileDto imageRequestDto) {
        String fullFileName = imageRequestDto.getFileName();
        byte[] imageFileData = imageRequestDto.getFileData();

        UploadFileRequestDto requestDto = UploadFileRequestDto.builder()
                .folderName(IMAGE_FOLDER_NAME)
                .fileName(fullFileName)
                .fileData(imageFileData)
                .build();

        String imageUrl = s3Service.uploadFile(requestDto);

        String originFileName = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
        String fileExtension = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);

        Image image = Image.builder()
                .url(imageUrl)
                .originalFileName(originFileName)
                .fileExtension(fileExtension)
                .fileSize((long) imageFileData.length)
                .build();
        Image savedImage = imageRepository.save(image);

        return ImageResponseDto.builder()
                .imageId(savedImage.getId())
                .imageUrl(savedImage.getUrl())
                .build();
    }

    @Transactional()
    public void deleteImageById(long id) {
        Image image = this.getImageById(id);
        s3Service.deleteFile(image.getUrl());
        imageRepository.delete(image);
    }

    @Transactional()
    public void deleteImageByUrl(String url) {
        Image image = this.getImageByUrl(url);
        s3Service.deleteFile(image.getUrl());
        imageRepository.delete(image);
    }

    @Transactional(readOnly = true)
    public ImageDataResponseDto getImageDataById(long id) {
        Image image = this.getImageById(id);
        var imageFileData = s3Service.getFile(image.getUrl());

        return ImageDataResponseDto.builder()
                .originalFileName(image.getOriginalFileName())
                .fileExtension(image.getFileExtension())
                .imageFileData(imageFileData)
                .build();
    }

    @Transactional(readOnly = true)
    public ImageDataResponseDto getImageDataByUrl(String url) {
        Image image = this.getImageByUrl(url);
        var imageFileData = s3Service.getFile(image.getUrl());

        return ImageDataResponseDto.builder()
                .originalFileName(image.getOriginalFileName())
                .fileExtension(image.getFileExtension())
                .imageFileData(imageFileData)
                .build();
    }
}
