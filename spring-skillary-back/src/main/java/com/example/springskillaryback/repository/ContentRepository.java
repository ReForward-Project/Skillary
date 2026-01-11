package com.example.springskillaryback.repository;

import com.example.springskillaryback.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Byte> {
	Optional<Content> findByTitle(String title);

}