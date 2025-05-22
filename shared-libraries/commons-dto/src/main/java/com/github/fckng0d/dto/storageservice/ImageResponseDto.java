package com.github.fckng0d.dto.storageservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageResponseDto {
    private Long imageId;
    private String imageUrl;
}
