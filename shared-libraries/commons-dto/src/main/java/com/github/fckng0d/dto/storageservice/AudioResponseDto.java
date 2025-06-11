package com.github.fckng0d.dto.storageservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AudioResponseDto {
    private Long audioId;
    private String audioUrl;
    private Short durationSeconds;
}
