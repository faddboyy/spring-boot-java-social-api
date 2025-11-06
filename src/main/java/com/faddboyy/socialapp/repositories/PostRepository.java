package com.faddboyy.socialapp.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.faddboyy.socialapp.entities.Post;

public interface PostRepository extends JpaRepository<Post, Integer> {
    
    Page<Post> findByUserId(Integer userId, Pageable pageable);

    @Query("SELECT p FROM Post p WHERE p.caption LIKE %:keyword%")
    Page<Post> searchByCaption(@Param("keyword") String keyword, Pageable pageable);
}