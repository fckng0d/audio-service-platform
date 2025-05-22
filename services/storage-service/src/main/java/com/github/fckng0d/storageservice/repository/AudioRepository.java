package com.github.fckng0d.storageservice.repository;

import com.github.fckng0d.storageservice.domain.Audio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AudioRepository extends JpaRepository<Audio, Long> {
    Optional<Audio> findByUrl(String url);
}
