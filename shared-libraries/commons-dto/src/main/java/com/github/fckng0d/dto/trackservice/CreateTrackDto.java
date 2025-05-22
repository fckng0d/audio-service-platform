package com.github.fckng0d.dto.trackservice;

import com.github.fckng0d.dto.Language;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.UploadFileDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
public class CreateTrackDto {
    @NotNull
    private UUID albumId;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @NotEmpty
    private List<Language> languages;

    @NotNull
    @NotEmpty
    private List<MusicGenre> genres;

    @NotNull
    @NotEmpty
    private List<UUID> musicianIds;

    private String lyrics;

    private boolean isExplicit;

    private UploadFileDto coverImage;

    @NotNull
    private UploadFileDto trackFile;
}
