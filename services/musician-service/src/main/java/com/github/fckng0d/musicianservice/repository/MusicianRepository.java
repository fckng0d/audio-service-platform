package com.github.fckng0d.musicianservice.repository;

import com.github.fckng0d.musicianservice.domain.Musician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, UUID> {
    Optional<Musician> findByNickname(String nickname);

    Optional<Musician> findByUserId(UUID userId);

    boolean existsByNickname(String nickname);

}
