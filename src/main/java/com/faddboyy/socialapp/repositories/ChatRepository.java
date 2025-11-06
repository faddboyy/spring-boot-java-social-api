package com.faddboyy.socialapp.repositories;

import com.faddboyy.socialapp.entities.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    
    /**
     * Mencari semua Chat di mana seorang user (berdasarkan userId)
     * adalah bagian dari list 'users' di dalam chat tersebut.
     */
    public List<Chat> findByUsersId(Integer userId);

        @Query("SELECT c FROM Chat c " +
           "JOIN c.users u1 " +
           "JOIN c.users u2 " +
           "WHERE u1.id = :userId1 " +
           "AND u2.id = :userId2 " +
           "AND SIZE(c.users) = 2")
    public Optional<Chat> findChatByTwoUsers(
            @Param("userId1") Integer userId1, 
            @Param("userId2") Integer userId2
    );
}