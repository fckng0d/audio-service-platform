package com.github.fckng0d.authenticationservice.repository;

import com.github.fckng0d.authenticationservice.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
    Optional<RefreshToken> findByToken(String token);

    List<RefreshToken> findAllByUserId(UUID userId);

    Optional<RefreshToken> findByIdAndExpiryDateAfter(UUID id, Instant date);

    Optional<RefreshToken> findByTokenAndExpiryDateAfter(String token, Instant now);

    void deleteByUserId(UUID userId);

    void deleteByToken(String token);

    void deleteAllByExpiryDateBefore(Instant now);
}
