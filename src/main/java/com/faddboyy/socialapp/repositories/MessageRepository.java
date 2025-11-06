package com.faddboyy.socialapp.repositories;

import com.faddboyy.socialapp.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {
    
    /**
     * Menemukan semua pesan berdasarkan ID chat.
     * Spring Data JPA akan otomatis membuat query-nya.
     */
    public List<Message> findByChatId(Integer chatId);
}