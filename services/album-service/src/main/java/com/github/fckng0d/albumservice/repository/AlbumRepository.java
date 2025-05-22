package com.github.fckng0d.albumservice.repository;

import com.github.fckng0d.albumservice.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<Album, UUID> {
}
