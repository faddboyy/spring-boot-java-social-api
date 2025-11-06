package com.faddboyy.socialapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.faddboyy.socialapp.entities.Reels;
import java.util.List;

public interface ReelRepository extends JpaRepository<Reels, Integer> { // Diperbaiki: Long, bukan Integer
    
    // Spring Data JPA akan otomatis membuat query
    // "SELECT r FROM Reels r WHERE r.user.id = :userId"
    List<Reels> findByUserId(Integer userId); 
}