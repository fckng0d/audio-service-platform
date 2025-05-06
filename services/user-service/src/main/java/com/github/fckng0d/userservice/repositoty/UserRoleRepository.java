package com.github.fckng0d.userservice.repositoty;

import com.github.fckng0d.userservice.domain.User;
import com.github.fckng0d.userservice.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Byte> {
    Optional<UserRole> findByName(String name);
    boolean existsByName(String name);
}
