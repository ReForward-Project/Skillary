package com.example.springskillaryback.repository;

import com.example.springskillaryback.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUserId(Byte userId);

    void deleteByUserId(Byte userId);
}