package com.github.fckng0d.audioservice.repository;

import com.github.fckng0d.audioservice.domain.Track;
import com.github.fckng0d.dto.MusicGenre;
import com.github.fckng0d.dto.trackservice.TrackPreviewResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByName(String name);

    List<Track> findAllByAlbumId(UUID albumId);

    List<Track> getAllByGenresContaining(List<MusicGenre> genres);
}
