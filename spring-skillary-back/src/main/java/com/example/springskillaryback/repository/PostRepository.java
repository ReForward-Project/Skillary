package com.example.springskillaryback.repository;

import com.example.springskillaryback.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Byte> { }
