package com.github.fckng0d.dto.storageservice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioDataResponseDto {
    @NotBlank
    private String originalFileName;

    @NotBlank
    private String fileExtension;

    @NotNull
    private byte[] audioFileData;
}

