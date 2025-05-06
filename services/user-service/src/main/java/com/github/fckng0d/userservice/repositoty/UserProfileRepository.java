package com.github.fckng0d.userservice.repositoty;

import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {
    List<UserProfile> findByRegistrationDate(Instant registrationDate);
}
