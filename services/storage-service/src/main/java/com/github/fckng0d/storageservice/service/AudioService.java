package com.github.fckng0d.storageservice.service;

import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.storageservice.UploadFileRequestDto;
import com.github.fckng0d.dto.storageservice.AudioDataResponseDto;
import com.github.fckng0d.dto.storageservice.AudioResponseDto;
import com.github.fckng0d.storageservice.domain.Audio;
import com.github.fckng0d.storageservice.exception.AudioNotFoundException;
import com.github.fckng0d.storageservice.repository.AudioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AudioService {
    private final AudioRepository audioRepository;
    private static final String AUDIO_FOLDER_NAME = "audio";
    private final S3Service s3Service;

    @Transactional(readOnly = true)
    public Audio getAudioById(long id) {
        return audioRepository.findById(id)
                .orElseThrow(() -> new AudioNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Audio getAudioByUrl(String url) {
        return audioRepository.findByUrl(url)
                .orElseThrow(() -> new AudioNotFoundException(url));
    }

    @Transactional
    public AudioResponseDto uploadAudio(UploadFileDto imageRequestDto) {
        String fullFileName = imageRequestDto.getFileName();
        byte[] imageFileData = imageRequestDto.getFileData();

        UploadFileRequestDto requestDto = UploadFileRequestDto.builder()
                .folderName(AUDIO_FOLDER_NAME)
                .fileName(fullFileName)
                .fileData(imageFileData)
                .build();

        String audioUrl = s3Service.uploadFile(requestDto);

        String originFileName = fullFileName.substring(0, fullFileName.lastIndexOf('.'));
        String fileExtension = fullFileName.substring(fullFileName.lastIndexOf('.') + 1);

        Audio audio = Audio.builder()
                .url(audioUrl)
                .originalFileName(originFileName)
                .fileExtension(fileExtension)
                .fileSize((long) imageFileData.length)
                .build();
        Audio savedAudio = audioRepository.save(audio);

        return AudioResponseDto.builder()
                .audioId(savedAudio.getId())
                .audioUrl(savedAudio.getUrl())
                .build();
    }

    @Transactional()
    public void deleteAudioById(long id) {
        Audio audio = this.getAudioById(id);
        s3Service.deleteFile(audio.getUrl());
        audioRepository.delete(audio);
    }

    @Transactional()
    public void deleteAudioByUrl(String url) {
        Audio audio = this.getAudioByUrl(url);
        s3Service.deleteFile(audio.getUrl());
        audioRepository.delete(audio);
    }

    @Transactional(readOnly = true)
    public AudioDataResponseDto getAudioDataById(long id) {
        Audio audio = this.getAudioById(id);
        var imageFileData = s3Service.getFile(audio.getUrl());

        return AudioDataResponseDto.builder()
                .originalFileName(audio.getOriginalFileName())
                .fileExtension(audio.getFileExtension())
                .audioFileData(imageFileData)
                .build();
    }

    @Transactional(readOnly = true)
    public AudioDataResponseDto getAudioDataByUrl(String url) {
        Audio audio = this.getAudioByUrl(url);
        var imageFileData = s3Service.getFile(audio.getUrl());

        return AudioDataResponseDto.builder()
                .originalFileName(audio.getOriginalFileName())
                .fileExtension(audio.getFileExtension())
                .audioFileData(imageFileData)
                .build();
    }
}
