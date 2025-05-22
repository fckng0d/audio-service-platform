package com.github.fckng0d.dto.musicianservice;

import com.github.fckng0d.dto.UploadFileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMusicianDto {
    @NotNull
    private UUID userId;

    @NotBlank
    @Size(min = 3, max = 50)
    private String nickname;

    @Size(max = 255)
    private String bio;

    private UploadFileDto avatarImage;

    private UploadFileDto headerImage;
}


