package com.github.fckng0d.dto.albumservice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlbumPreviewResponseDto {
    private UUID albumId;
    private String name;
    private String coverImageUrl;
    private Short releaseYear;
    private Boolean isExplicit;
    private List<String> musicianNicknames;
    private Long auditionCount;
}