package com.github.fckng0d.albumservice.repository;

import com.github.fckng0d.albumservice.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {
    @Query("SELECT a FROM Album a WHERE :nickname MEMBER OF a.musicianNicknames")
    List<Album> findAllByMusicianNickname(String nickname);

    @Query("SELECT a FROM Album a WHERE :nickname MEMBER OF a.trackGuestNicknames")
    List<Album> findAllByTrackGuestNickname(String nickname);

    UUID id(UUID id);
}
