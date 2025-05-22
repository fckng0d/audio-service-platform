package com.github.fckng0d.storageservice.repository;

import com.github.fckng0d.storageservice.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUrl(String url);
}
