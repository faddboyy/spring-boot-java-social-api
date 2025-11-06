package com.faddboyy.socialapp.controllers;

import com.faddboyy.socialapp.dto.MessageDto;
import com.faddboyy.socialapp.request.CreateMessageRequestDto;
import com.faddboyy.socialapp.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages") 
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @PostMapping("/{chatId}")
    public ResponseEntity<MessageDto> createMessage(
            @RequestBody CreateMessageRequestDto request,
            @PathVariable Integer chatId,
            Principal principal 
    ) {
        MessageDto message = messageService.createMessage(
            request, 
            chatId, 
            principal.getName()
        );
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    /**
     * Endpoint untuk mengambil semua riwayat pesan dari satu chat room.
     */
    @GetMapping("/{chatId}")
    public ResponseEntity<List<MessageDto>> findMessagesByChatId(
            @PathVariable Integer chatId,
            Principal principal // Untuk validasi keamanan
    ) {
        List<MessageDto> messages = messageService.findMessagesByChatId(
            chatId, 
            principal.getName()
        );
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    /**
     * [ENDPOINT BARU] Endpoint untuk menghapus pesan.
     * Hanya user yang mengirimnya yang bisa menghapus.
     */
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable Integer messageId,
            Principal principal // Untuk verifikasi pemilik
    ) {
        String message = messageService.deleteMessage(messageId, principal.getName());
        
        // Mengembalikan response JSON yang rapi
        return new ResponseEntity<>(Map.of("message", message), HttpStatus.OK);
    }
}