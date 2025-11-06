package com.faddboyy.socialapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.faddboyy.socialapp.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
}
