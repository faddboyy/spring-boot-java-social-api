package com.faddboyy.socialapp.controllers;

import com.faddboyy.socialapp.dto.ChatDto;
import com.faddboyy.socialapp.request.CreateChatRequestDto;
import com.faddboyy.socialapp.services.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal; 
import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    
    @Autowired
    private ChatService chatService;


    @PostMapping("/") 
    public ResponseEntity<ChatDto> createChat(
            @RequestBody CreateChatRequestDto request,
            Principal principal 
    ) {
       
        ChatDto createdChat = chatService.createChat(request, principal.getName());
        return new ResponseEntity<>(createdChat, HttpStatus.CREATED);
    }
  
    @GetMapping("/") 
    public ResponseEntity<List<ChatDto>> findChatsByCurrentUser(
            Principal principal 
    ) {
        
        List<ChatDto> chats = chatService.findChatsByEmail(principal.getName());
        return new ResponseEntity<>(chats, HttpStatus.OK);
    }

    /**
     * (Tetap sama) Endpoint untuk mengambil detail satu chat.
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<ChatDto> findChatById(
            @PathVariable Integer chatId
    ) {
        ChatDto chat = chatService.findChatById(chatId);
        return new ResponseEntity<>(chat, HttpStatus.OK);
    }
}