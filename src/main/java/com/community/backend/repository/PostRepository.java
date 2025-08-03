package com.community.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.community.backend.entity.Post;

  public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();
}


