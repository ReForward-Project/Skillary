package com.example.springskillaryback.repository;

import com.example.springskillaryback.domain.Creator;
import com.example.springskillaryback.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Byte> {
    boolean existsByUser_UserId(Byte userUserId);

    List<Creator> user(User user);

    Optional<Creator> findByUser_UserId(Byte userId);
}