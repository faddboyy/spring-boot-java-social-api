package com.faddboyy.socialapp.services;

import com.faddboyy.socialapp.dto.ChatDto;
import com.faddboyy.socialapp.entities.Chat;
import com.faddboyy.socialapp.request.CreateChatRequestDto;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatService {
    
    /**
     * Membuat chat baru berdasarkan email pembuat (dari Principal).
     */
    ChatDto createChat(CreateChatRequestDto request, String creatorEmail);

    /**
     * Menemukan semua chat milik user berdasarkan email (dari Principal).
     */
    List<ChatDto> findChatsByEmail(String email);

    /**
     * Menemukan chat spesifik berdasarkan ID chat.
     */
    ChatDto findChatById(Integer chatId);

    Chat findChatEntityById(Integer chatId);

}