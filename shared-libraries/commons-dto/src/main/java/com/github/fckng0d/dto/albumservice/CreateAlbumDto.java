package com.github.fckng0d.dto.albumservice;


import com.github.fckng0d.dto.UploadFileDto;
import com.github.fckng0d.dto.trackservice.CreateTrackDto;
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
public class CreateAlbumDto {
    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotNull
    @NotEmpty
    private List<String> musicianNicknames;

    private UploadFileDto coverImage;

    @NotNull
    @NotEmpty
    private List<CreateTrackDto> tracks;
}
