package com.github.fckng0d.dto.s3service;

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
public class UploadFileRequestDto {
    @NotBlank
    private String folderName;

    @NotBlank
    private String fileName;

    @NotNull
    private byte[] fileData;
}
